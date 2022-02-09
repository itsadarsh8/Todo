package com.example.zuper_todo.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.zuper_todo.R
import com.example.zuper_todo.databinding.TagsChildItemBinding
import com.example.zuper_todo.models.Data
import kotlinx.android.synthetic.main.todo_single_item.view.*

class TagChildAdapter (private val list:List<Data>):RecyclerView.Adapter<TagChildAdapter.MyViewHolder>(){

   inner class MyViewHolder(val viewDataBinding: TagsChildItemBinding):RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TagChildAdapter.MyViewHolder {
   val binding= TagsChildItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagChildAdapter.MyViewHolder, position: Int) {
        if (list[position].priority.equals("HIGH")) {
            holder.viewDataBinding.priorityTag.setImageResource(R.drawable.ic_high_priority)
        } else if (list[position].priority.equals("LOW")) {
            holder.viewDataBinding.priorityTag.setImageResource(R.drawable.ic_low_priority)
        } else {
            holder.viewDataBinding.priorityTag.setImageResource(R.drawable.ic_medium_priority)
        }
        holder.viewDataBinding.todoTag.text=list[position].title
        Log.i("zzzzz",list[position].priority)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}