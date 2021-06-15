package com.weinstudio.affari.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weinstudio.affari.R
import com.weinstudio.affari.data.Category
import com.weinstudio.affari.data.Task

class DataAdapter(
    private var context: Context

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_TASK = 1
        const val TYPE_NOTIFICATION = 2
    }

    private var data: List<Any> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> CategoryViewHolder(
                LayoutInflater.from(context).inflate(R.layout.view_category, parent, false)
            )

            TYPE_TASK -> TaskViewHolder(
                LayoutInflater.from(context).inflate(R.layout.view_task, parent, false)
            )
            else -> null!!
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_CATEGORY -> (holder as CategoryViewHolder).bind(data[position] as Category)
            TYPE_TASK -> (holder as TaskViewHolder).bind(data[position] as Task)
            else -> TODO()
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(data: List<Any>) {
        this.data = data
        notifyDataSetChanged()
    }

    private inner class CategoryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tv_title)
        fun bind(category: Category) {
            title.text = category.title
        }
    }

    private inner class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.tv_title)
        fun bind(task: Task) {
            message.text = task.title
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Category -> TYPE_CATEGORY
            is Task -> TYPE_TASK
            else -> TYPE_NOTIFICATION
        }
    }
}