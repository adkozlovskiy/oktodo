package com.weinstudio.memoria.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.weinstudio.memoria.R
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.util.enums.Priority
import java.text.SimpleDateFormat
import java.util.*

class ProblemsAdapter(
    private val context: Context

) : RecyclerView.Adapter<ProblemsAdapter.TaskViewHolder>() {

    private var oldProblems: List<Problem> = listOf()
    private var actualProblems: List<Problem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_problem, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(context, actualProblems[position])
    }

    override fun getItemCount(): Int = actualProblems.size

    fun setItems(newProblems: MutableList<Problem>) {
        oldProblems = actualProblems.toList()
        actualProblems = newProblems.toList()

        val diffResult: DiffUtil.DiffResult = calculateDiff(oldProblems, actualProblems)
        diffResult.dispatchUpdatesTo(this)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvDeadline = itemView.findViewById<TextView>(R.id.tv_deadline)
        private val ivPriority = itemView.findViewById<ImageView>(R.id.iv_priority)

        fun bind(context: Context, problem: Problem) {
            tvTitle.text = problem.title

            val dateFormat = SimpleDateFormat(" dd MMMM yyyy HH:mm", Locale.getDefault())
            if (problem.deadline != null) {
                if (!tvDeadline.isVisible) {
                    tvDeadline.visibility = View.VISIBLE
                }

                val date = Date(problem.deadline)
                val dateString = context.getString(R.string.until) + dateFormat.format(date)
                tvDeadline.text = dateString

            } else {
                tvDeadline.visibility = View.GONE
            }

            when (problem.priority) {
                Priority.HIGH_PRIORITY -> ivPriority.setImageResource(R.drawable.ic_high_priority)
                Priority.LOW_PRIORITY -> ivPriority.setImageResource(R.drawable.ic_low_priority)
            }
        }
    }

    private fun calculateDiff(
        oldProblems: List<Problem>,
        newProblems: List<Problem>
    ): DiffUtil.DiffResult {
        val callback = ProblemsDiffCallback(
            oldProblems = oldProblems,
            newProblems = newProblems
        )
        return DiffUtil.calculateDiff(callback)
    }
}