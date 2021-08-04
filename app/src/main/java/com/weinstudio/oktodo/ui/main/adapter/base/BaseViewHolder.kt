package com.weinstudio.oktodo.ui.main.adapter.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.weinstudio.oktodo.data.model.ListItem

abstract class BaseViewHolder<out V : ViewBinding, in I : ListItem>(
    binding: V

) : RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind(item: I)

}