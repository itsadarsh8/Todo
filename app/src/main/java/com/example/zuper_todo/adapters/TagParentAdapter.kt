package com.example.zuper_todo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.zuper_todo.databinding.TagsParentItemBinding
import com.example.zuper_todo.models.TagsParentModel

 class TagParentAdapter(val list:List<TagsParentModel>):RecyclerView.Adapter<TagParentAdapter.MyViewHolder>() {
    inner class MyViewHolder(val viewDataBinding: TagsParentItemBinding):RecyclerView.ViewHolder(viewDataBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=TagsParentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewDataBinding.tagName.text=list[position].tagName
        holder.viewDataBinding.tagRvChild.apply {
            adapter=TagChildAdapter(list[position].tagsChildList)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}