package com.weinstudio.memoria.ui.main.adapter.fingerprint

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.weinstudio.memoria.R
import com.weinstudio.memoria.data.ListItem
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.databinding.LayoutProblemBinding
import com.weinstudio.memoria.ui.main.adapter.base.BaseFingerprint
import com.weinstudio.memoria.ui.main.adapter.base.BaseViewHolder
import com.weinstudio.memoria.data.entity.enums.Priority
import java.text.SimpleDateFormat
import java.util.*

class ProblemFingerprint(ctx: Context) : BaseFingerprint<LayoutProblemBinding, Problem> {

    val sdf = SimpleDateFormat(" dd MMMM yyyy HH:mm", Locale.getDefault())

    val colorRed = ctx.getColor(R.color.red_primary)
    val colorSecondary = ctx.getColor(R.color.text_secondary)
    val until = ctx.getString(R.string.until)

    override fun isRelativeItem(item: ListItem) = item is Problem

    override fun getLayoutId() = R.layout.layout_problem

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<LayoutProblemBinding, Problem> {
        val binding = LayoutProblemBinding.inflate(inflater, parent, false)
        return ProblemViewHolder(binding)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Problem>() {
        override fun areItemsTheSame(oldItem: Problem, newItem: Problem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Problem, newItem: Problem): Boolean {
            return oldItem == newItem
        }
    }

    override fun getDiffUtil() = diffUtil

    inner class ProblemViewHolder(private val binding: LayoutProblemBinding) :
        BaseViewHolder<LayoutProblemBinding, Problem>(binding) {

        private val tvDeadline = binding.tvDeadline
        private val ivPriority = binding.ivPriority

        override fun onBind(item: Problem) {
            binding.tvTitle.text = item.text

            if (!tvDeadline.isVisible) {
                tvDeadline.visibility = View.VISIBLE
            }

            if (!ivPriority.isVisible) {
                ivPriority.visibility = View.VISIBLE
            }

            val deadlineMillis = item.deadline

            if (deadlineMillis != null) { // If task has deadline.
                val deadlineDate = Date(deadlineMillis)
                val deadlineStr = until + sdf.format(deadlineDate)
                tvDeadline.text = deadlineStr

            } else {
                tvDeadline.visibility = View.GONE
            }

            if (!item.done) {
                if (deadlineMillis != null) {
                    val currentMillis = Date().time
                    tvDeadline.setTextColor(
                        if (currentMillis > deadlineMillis) {
                            colorRed

                        } else colorSecondary
                    )
                }

                when (item.priority) {
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
                    tvDeadline.setTextColor(colorSecondary)

                } else {
                    tvDeadline.visibility = View.GONE
                }

                ivPriority.setImageResource(R.drawable.ic_problem_done)
            }
        }
    }
}