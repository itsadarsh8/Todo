package com.example.zuper_todo.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.zuper_todo.R
import com.example.zuper_todo.api.RestApiService
import com.example.zuper_todo.models.Data
import com.example.zuper_todo.models.TodoInfoData

//I'll not use notify_data_change (not efficient as it loads whole data again)-> I'll use DiffUtil to load changed items only.

class TodoAdapter: RecyclerView.Adapter<TodoAdapter.TodosViewHolder>() {

    inner class TodosViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.titleTodo)
        val tag : TextView=itemView.findViewById(R.id.tag)
        val checkboxField: CheckBox=itemView.findViewById(R.id.checkboxField)

    }
    private val differCallback=object:DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosViewHolder {
        return TodosViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodosViewHolder, position: Int) {


        val todo= differ.currentList[position]
        holder.itemView.apply {

           //TODO UI binding here
            holder.title.text= todo.title
            holder.tag.text=todo.tag
            holder.checkboxField!!.isChecked=todo.is_completed

            holder.checkboxField.setOnClickListener(View.OnClickListener {
                toggleStatus(holder.checkboxField.isChecked,todo,position)
            })

            //TODO Toggle status of todos
//            setOnItemClickListener {
//
//
//            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

//    private var onItemClickListener:((Data)-> Unit)?=null
//    fun setOnItemClickListener(listener:(Data)-> Unit){
//        onItemClickListener=listener;
//    }

    private  fun toggleStatus(isCompleted:Boolean,todo:Data,position:Int){
//        onItemClickListener?.let{it(todo)}
        val apiService= RestApiService()
        val todoIndoData= TodoInfoData(todo.title,todo.author,todo.tag,isCompleted,todo.priority,null)

        apiService.toggleStatus(todo.id,todoIndoData) {
            if (it?.id != null){
                //SUCCESS ANIMATION (optional)
            }else{
                Log.e("TODOADAPTER","Error creating new TODO")
            }
        }
    }
}