package com.weinstudio.affari.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.weinstudio.affari.data.Problem

class ProblemsCallbackImpl(
    private val oldProblems: List<Problem>,
    private val newProblems: List<Problem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldProblems.size

    override fun getNewListSize() = newProblems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProblem = oldProblems[oldItemPosition]
        val newProblem = newProblems[newItemPosition]

        return oldProblem.id == newProblem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProblems[oldItemPosition] == newProblems[newItemPosition]
    }
}