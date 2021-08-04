package com.weinstudio.oktodo.ui.main.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.weinstudio.oktodo.data.model.ListItem
import com.weinstudio.oktodo.ui.main.adapter.base.BaseFingerprint

class ItemDiffCallback(
    private val fingerprints: List<BaseFingerprint<*, *>>
) : DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }

        return getItemCallback(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }

        return getItemCallback(oldItem).areContentsTheSame(oldItem, newItem)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getItemCallback(item: ListItem): DiffUtil.ItemCallback<ListItem> =
        fingerprints.find { it.isRelativeItem(item) }
            ?.getDiffUtil()
            ?.let { it as DiffUtil.ItemCallback<ListItem> }
            ?: throw IllegalArgumentException()

}