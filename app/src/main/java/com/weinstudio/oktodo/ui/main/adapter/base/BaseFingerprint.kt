package com.weinstudio.oktodo.ui.main.adapter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.weinstudio.oktodo.data.model.ListItem

interface BaseFingerprint<V : ViewBinding, I : ListItem> {

    fun isRelativeItem(item: ListItem): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<V, I>

    fun getDiffUtil(): DiffUtil.ItemCallback<I>

}