package com.weinstudio.memoria.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.weinstudio.memoria.data.entity.ListItem
import com.weinstudio.memoria.ui.main.adapter.base.BaseFingerprint
import com.weinstudio.memoria.ui.main.adapter.base.BaseViewHolder

class FingerprintAdapter(
    private val fingerprints: List<BaseFingerprint<*, *>>
) : ListAdapter<ListItem, BaseViewHolder<ViewBinding, ListItem>>(
    FingerprintDiffCallback(fingerprints)
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
        Log.d("TAG", "onBindViewHolder: $currentList")
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return fingerprints.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw IllegalArgumentException()
    }
}