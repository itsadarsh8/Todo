package com.example.zuper_todo

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import com.example.zuper_todo.adapters.TagParentAdapter
import com.example.zuper_todo.api.RetrofitInstance
import com.example.zuper_todo.databinding.ActivityMainBinding
import com.example.zuper_todo.databinding.ActivityTagsBinding
import com.example.zuper_todo.models.Data
import com.example.zuper_todo.models.TagsParentModel
import com.example.zuper_todo.models.TodoResponse
import com.example.zuper_todo.utils.Constants.INTENT_TAG
import kotlinx.android.synthetic.main.tag_activity_custom_title_bar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class TagsActivity : AppCompatActivity() {


    private val TAG="TagsActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main)
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.tag_activity_custom_title_bar)


        back_button.setOnClickListener(View.OnClickListener {
            finish()
        })
        val tagList= mutableListOf<String>()
        val todoParentList= mutableListOf<TagsParentModel>()
//        CoroutineScope(Dispatchers.IO).launch {
//            Log.i(TAG, getAllTodos()!!.data[1].id.toString())
//            val todoList=getAllTodos()!!.data
            val todoList: ArrayList<Data> = intent.getSerializableExtra(INTENT_TAG) as ArrayList<Data>
            for (item in todoList){
                if(!tagList.contains(item.tag)){
                    tagList.add(item.tag)
                    val element= mutableListOf<Data>()
                    element.add(item)
                    todoParentList.add(TagsParentModel(element,item.tag))
                }else{
                    val position=tagList.indexOf(item.tag)
                    todoParentList[position].tagsChildList.add(item)
                }




            Log.i(TAG, todoParentList.toString())

            val binding:ActivityTagsBinding=DataBindingUtil.setContentView(this,R.layout.activity_tags)
                binding.mainRecycler.apply {
                    adapter=TagParentAdapter(todoParentList)
                }

        }




    }

}