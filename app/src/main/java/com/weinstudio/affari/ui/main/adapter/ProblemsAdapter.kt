package com.weinstudio.affari.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.weinstudio.affari.R
import com.weinstudio.affari.data.Problem
import com.weinstudio.affari.data._enum.Priority
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
        holder.bind(actualProblems[position])
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

        fun bind(problem: Problem) {
            tvTitle.text = problem.title

            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            if (problem.deadline != null) {
                val date = Date(problem.deadline)
                tvDeadline.text = dateFormat.format(date)

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