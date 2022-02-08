package com.example.zuper_todo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.zuper_todo.R
import com.example.zuper_todo.models.DropDownModel

class CustomDropDownAdapter(val context: Context, var dataSource: List<DropDownModel>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_layout, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = dataSource.get(position).name
        if(dataSource.get(position).name=="Low"){
            vh.img.setImageResource(R.drawable.ic_low_priority)
        }
        if(dataSource.get(position).name=="Med"){
            vh.img.setImageResource(R.drawable.ic_medium_priority)
        }
        if(dataSource.get(position).name=="High"){
            vh.img.setImageResource(R.drawable.ic_high_priority)
        }
//        val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
//        vh.img.setBackgroundResource(id)

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        val img: ImageView

        init {
            label = row?.findViewById(R.id.spinner_text) as TextView
            img = row?.findViewById(R.id.spinner_ic) as ImageView
        }
    }

}