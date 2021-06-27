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
    private val ctx: Context
) : RecyclerView.Adapter<ProblemsAdapter.TaskViewHolder>() {

    private val sdf = SimpleDateFormat(" dd MMMM yyyy HH:mm", Locale.getDefault())

    // Saved problems list.
    private var oldProblems: List<Problem> = listOf()

    // Actual problems list.
    var actualProblems: List<Problem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.layout_problem, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(actualProblems[position])
    }

    override fun getItemCount(): Int = actualProblems.size

    fun setItems(newProblems: MutableList<Problem>) {
        oldProblems = actualProblems.toList()
        actualProblems = newProblems.toList()

        val diff: DiffUtil.DiffResult = calculateDiff(oldProblems, actualProblems)
        diff.dispatchUpdatesTo(this)
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        private val tvDeadline = view.findViewById<TextView>(R.id.tv_deadline)
        private val ivPriority = view.findViewById<ImageView>(R.id.iv_priority)

        fun bind(problem: Problem) {
            tvTitle.text = problem.title

            if (!tvDeadline.isVisible) {
                tvDeadline.visibility = View.VISIBLE
            }

            if (!ivPriority.isVisible) {
                ivPriority.visibility = View.VISIBLE
            }

            val deadlineMillis = problem.deadline

            if (deadlineMillis != null) { // If task has deadline.
                val deadlineDate = Date(deadlineMillis)
                val deadlineStr = ctx.getString(R.string.until) + sdf.format(deadlineDate)
                tvDeadline.text = deadlineStr

            } else {
                tvDeadline.visibility = View.GONE
            }

            if (!problem.isDone) {
                if (deadlineMillis != null) {
                    val currentMillis = Date().time

                    tvDeadline.setTextColor(
                        if (currentMillis > deadlineMillis) {
                            ctx.getColor(R.color.red_primary)

                        } else ctx.getColor(R.color.text_secondary)
                    )
                }

                when (problem.priority) {
                    Priority.HIGH -> {
                        ivPriority.setImageResource(R.drawable.ic_high_priority)
                    }

                    Priority.LOW -> {
                        ivPriority.setImageResource(R.drawable.ic_low_priority)
                    }

                    Priority.DEFAULT -> {
                        ivPriority.visibility = View.INVISIBLE
                    }
                }

            } else {
                if (deadlineMillis != null) {
                    tvDeadline.setTextColor(ctx.getColor(R.color.text_secondary))

                } else {
                    tvDeadline.visibility = View.GONE
                }

                ivPriority.setImageResource(R.drawable.ic_problem_done)
            }
        }
    }

    private fun calculateDiff(old: List<Problem>, new: List<Problem>): DiffUtil.DiffResult {
        val callback = ProblemsDiffCallback(oldItems = old, newItems = new)
        return DiffUtil.calculateDiff(callback)
    }
}