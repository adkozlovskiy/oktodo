package com.weinstudio.affari.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weinstudio.affari.R
import com.weinstudio.affari.data.Task

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    private var tasks: List<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateData(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvDesc: TextView = itemView.findViewById(R.id.tv_desc)

        fun bind(task: Task) = with(itemView) {
            tvTitle.text = task.title
            tvDesc.text = task.desc
        }
    }
}