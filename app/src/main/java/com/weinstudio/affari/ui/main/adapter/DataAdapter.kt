package com.weinstudio.affari.ui.main.adapter

import android.content.ClipData.Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.weinstudio.affari.R
import com.weinstudio.affari.data.Task


class DataAdapter(
    private var context: Context

) : RecyclerView.Adapter<DataAdapter.TaskViewHolder>() {

    private var data: MutableList<Task> = mutableListOf()

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(data: MutableList<Task>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun removeTask(position: Int): Task {
        val removedTask = data[position]
        data.removeAt(position)
        notifyItemRemoved(position)
        return removedTask
    }

    fun restoreTask(task: Task, position: Int) {
        data.add(position, task)
        notifyItemInserted(position)
    }

    class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.tv_title)
        var viewForeground: ConstraintLayout = itemView.findViewById(R.id.view_foreground)

        fun bind(task: Task) {
            message.text = task.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_task, parent, false)
        )
    }
}