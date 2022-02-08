package com.example.zuper_todo.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.zuper_todo.R
import com.example.zuper_todo.api.RestApiService
import com.example.zuper_todo.models.Data
import com.example.zuper_todo.models.TodoInfoData
import kotlinx.android.synthetic.main.todo_single_item.view.*

//I'll not use notify_data_change (not efficient as it loads whole data again)-> I'll use DiffUtil to load changed items only.

class TodoAdapter: RecyclerView.Adapter<TodoAdapter.TodosViewHolder>() {

    inner class TodosViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.titleTodo)
        val tag : TextView=itemView.findViewById(R.id.tag)
        val checkboxField: ImageView=itemView.findViewById(R.id.checkboxField)
        val itemLayout:RelativeLayout=itemView.findViewById(R.id.item_layout)


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

            if(todo.is_completed){
                holder.checkboxField.setImageResource(R.drawable.ic_checked)
                holder.checkboxField.setTag(R.drawable.ic_checked)
            }
            else{
                holder.checkboxField.setImageResource(R.drawable.ic_notchecked)
                holder.checkboxField.setTag(R.drawable.ic_notchecked)
            }

            if(todo.priority!=null) {
                if (todo.priority.equals("HIGH")) {
                    priorityIc.setImageResource(R.drawable.ic_high_priority)
                } else if (todo.priority.equals("LOW")) {
                    priorityIc.setImageResource(R.drawable.ic_low_priority)
                } else {
                    priorityIc.setImageResource(R.drawable.ic_medium_priority)
                }
            }
            holder.checkboxField.setOnClickListener(View.OnClickListener {
//                if(todox.is_completed){
//                    holder.checkboxField.setImageResource(R.drawable.ic_notchecked)
//                }
//                else{
//                    holder.checkboxField.setImageResource(R.drawable.ic_checked)
//                }


                val drawable:Int=checkboxField.getTag() as Int
                if(drawable==R.drawable.ic_checked){
                    holder.checkboxField.setImageResource(R.drawable.ic_notchecked)
                    holder.checkboxField.setTag(R.drawable.ic_notchecked)
                    toggleStatus(todo,false)
                }
                else if(drawable==R.drawable.ic_notchecked){
                    holder.checkboxField.setImageResource(R.drawable.ic_checked)
                    holder.checkboxField.setTag(R.drawable.ic_checked)
                    toggleStatus(todo,true)
                }





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


    private  fun toggleStatus(todo:Data,isCompleted:Boolean){

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