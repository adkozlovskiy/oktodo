package com.weinstudio.memoria.ui.main.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.weinstudio.memoria.data.ListItem
import com.weinstudio.memoria.ui.main.adapter.base.BaseFingerprint

class ItemDiffCallback(
    private val fingerprints: List<BaseFingerprint<*, *>>
) : DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        if (oldItem::class != newItem::class) return false
        return getItemCallback(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        if (oldItem::class != newItem::class) return false
        return getItemCallback(oldItem).areContentsTheSame(oldItem, newItem)
    }

    private fun getItemCallback(item: ListItem): DiffUtil.ItemCallback<ListItem> =
        fingerprints.find { it.isRelativeItem(item) }
            ?.getDiffUtil()
            ?.let { it as DiffUtil.ItemCallback<ListItem> }
            ?: throw IllegalArgumentException()

}