package com.weinstudio.memoria.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.weinstudio.memoria.data.entity.Problem

class ProblemsDiffCallback(
    private val oldItems: List<Problem>,
    private val newItems: List<Problem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    // Maybe I can compare by id, but while it works, I won't touch it :)
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}