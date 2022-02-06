package com.example.zuper_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zuper_todo.adapters.TodoAdapter
import com.example.zuper_todo.db.TodoDatabase
import com.example.zuper_todo.repository.TodoRepository
import com.example.zuper_todo.utils.Constants.SEARCH_TODO_TIME_DELAY
import com.example.zuper_todo.utils.Resource
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

    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val todoRepository=TodoRepository(TodoDatabase(this))
        val viewModelProviderFactory=TodoViewModelProviderFactory(todoRepository)
        viewModel=ViewModelProvider(this, viewModelProviderFactory).get(TodoViewModel::class.java)
        setupRecyclerView()

        viewModel.todoLiveData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let{todoResponse ->
                        todoAdapter.differ.submitList(todoResponse.data)
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let{message ->
                        Log.e(TAG,"Error occured: $message")
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
                        viewModel.getTodo("Ranjith")
                    }
                }
            }
        }
    }
    private fun hideProgressBar(){
        // TODO  hide progress bar

    }

    private fun showProgressBar(){
        // TODO  show progress bar

    }



    private fun setupRecyclerView(){
        todoAdapter= TodoAdapter()
        rvTodo.apply {
            adapter = todoAdapter

            //check context
            layoutManager = LinearLayoutManager(context)
        }

    }
}