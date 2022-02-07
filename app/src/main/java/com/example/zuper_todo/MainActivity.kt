package com.example.zuper_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zuper_todo.adapters.TodoAdapter
import com.example.zuper_todo.api.RestApiService
import com.example.zuper_todo.db.TodoDatabase
import com.example.zuper_todo.models.TodoInfoData
import com.example.zuper_todo.repository.TodoRepository
import com.example.zuper_todo.utils.Constants.AUTHOR_NAME
import com.example.zuper_todo.utils.Constants.QUERY_PAGE_SIZE
import com.example.zuper_todo.utils.Constants.SEARCH_TODO_TIME_DELAY
import com.example.zuper_todo.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: TodoViewModel
    lateinit var todoAdapter: TodoAdapter

    private val rvTodo by lazy{
        findViewById<RecyclerView>(R.id.rvTodo)
    }
    private val searchBar by lazy{
        findViewById<EditText>(R.id.searchBar)
    }
    private val btnBottomSheet by lazy{
        findViewById<Button>(R.id.buttonBottomSheet)
    }

    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val todoRepository=TodoRepository(TodoDatabase(this))
        val viewModelProviderFactory=TodoViewModelProviderFactory(application, todoRepository)
        viewModel=ViewModelProvider(this, viewModelProviderFactory).get(TodoViewModel::class.java)
        setupRecyclerView()

        viewModel.todoLiveData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let{todoResponse ->
                        todoAdapter.differ.submitList(todoResponse.data.toList())
                        //integer division thus 1+ last page will be empty thus 1= 2
                        val totalPages= todoResponse.total_records/ QUERY_PAGE_SIZE + 2
                        islastPage=viewModel.todoPage==totalPages
                        if(islastPage){
                            //To prevent overlapping of progress bar
                        rvTodo.setPadding(0,0,0,0)
                    }
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let{message ->
                        Log.e(TAG,"Error occured: $message")
                        Toast.makeText(this, "An error occured: $message",Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading-> {
                    showProgressBar()
                }
            }

        })

        var job: Job?=null
        searchBar.addTextChangedListener{editable ->
            job?.cancel()
            job= MainScope().launch{
                delay(SEARCH_TODO_TIME_DELAY)

                editable?.let{
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchTodo(editable.toString())

                    }
                    else{
                        viewModel.getTodo(AUTHOR_NAME)
                    }
                }
            }
        }

        btnBottomSheet.setOnClickListener{
            val dialog= BottomSheetDialog(this)
            val view= layoutInflater.inflate(R.layout.bottom_sheet_dialog,null)
            val btnClose= view.findViewById<Button>(R.id.createButton)
//            val titleField=view.findViewById<EditText>(R.id.titleField)
//            val tagField=view.findViewWithTag<EditText>(R.id.tagField)
            btnClose.setOnClickListener{
                if(view.titleField.text.isEmpty() || view.tagField.text.isEmpty()){
                 Toast.makeText(this,"All fields are required",Toast.LENGTH_SHORT).show()
                }else {
                    createTodo(
                        view.titleField.text.toString(),
                        AUTHOR_NAME,
                        view.tagField.text.toString(),
                        false,
                        "HIGH"
                    )

                    dialog.dismiss()
                }



            }
            dialog.setContentView(view)
            dialog.show()
        }

    }

     fun createTodo(title:String, author:String,tag:String,isCompleted:Boolean,priority:String){
        val apiService=RestApiService()
        val todoIndoData=TodoInfoData(title,author,tag,isCompleted,priority,null)

        apiService.createTodo(todoIndoData) {
            if (it?.id != null){

            }else{
                Log.e(TAG,"Error creating new TODO")
            }
        }


    }

    private fun hideProgressBar(){
        // TODO  hide progress bar
        isLoading=false

    }

    private fun showProgressBar(){
        // TODO  show progress bar
        isLoading=true

    }

    var isLoading = false
    var islastPage = false
    var isScrolling = false

    val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx:Int, dy:Int){
            super.onScrolled(recyclerView, dx, dy)

            //To check if we are at the bottom
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndLastPage= !isLoading && !islastPage
            val isAtLastItem=firstVisibleItemPosition+visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount>= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.getTodo("Ranjith")
                isScrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }


    private fun setupRecyclerView(){
        todoAdapter= TodoAdapter()
        rvTodo.apply {
            adapter = todoAdapter

            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(this@MainActivity.scrollListener)
        }

    }
}