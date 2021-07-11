package com.weinstudio.oktodo.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.weinstudio.oktodo.data.ListItem
import com.weinstudio.oktodo.ui.main.adapter.base.BaseFingerprint
import com.weinstudio.oktodo.ui.main.adapter.base.BaseViewHolder
import com.weinstudio.oktodo.ui.main.adapter.util.ItemDiffCallback

class FingerprintAdapter(
    private val fingerprints: List<BaseFingerprint<*, *>>
) : ListAdapter<ListItem, BaseViewHolder<ViewBinding, ListItem>>(
    ItemDiffCallback(fingerprints)
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding, ListItem> {
        val inflater = LayoutInflater.from(parent.context)
        return fingerprints.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, ListItem> }
            ?: throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, ListItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return fingerprints.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw IllegalArgumentException()
    }
}