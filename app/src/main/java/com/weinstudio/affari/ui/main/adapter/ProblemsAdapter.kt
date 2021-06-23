package com.weinstudio.affari.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.weinstudio.affari.R
import com.weinstudio.affari.data.Problem

class ProblemsAdapter(
    private var context: Context

) : RecyclerView.Adapter<ProblemsAdapter.TaskViewHolder>() {

    private var problems: MutableList<Problem> = mutableListOf()

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(problems[position])
    }

    fun setItems(newProblems: MutableList<Problem>) {
        val oldProblems = problems
        this.problems = newProblems

        val diffResult: DiffUtil.DiffResult = calculateDiff(oldProblems, newProblems)
        diffResult.dispatchUpdatesTo(this)
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

    override fun getItemCount(): Int = problems.size

    fun removeProblem(position: Int): Problem {
        val removed = problems[position]
        problems.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    fun restoreProblem(problem: Problem, position: Int) {
        problems.add(position, problem)
        notifyItemInserted(position)
    }

    class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.tv_title)
        var viewForeground: ConstraintLayout = itemView.findViewById(R.id.view_foreground)

        fun bind(problem: Problem) {
            message.text = problem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_task, parent, false)
        )
    }
}