package com.weinstudio.oktodo.ui.main.adapter.fingerprint

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.data.model.ListItem
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.data.model.enums.Importance
import com.weinstudio.oktodo.databinding.LayoutProblemBinding
import com.weinstudio.oktodo.ui.main.adapter.base.BaseFingerprint
import com.weinstudio.oktodo.ui.main.adapter.base.BaseViewHolder
import com.weinstudio.oktodo.util.getColorCompat
import com.weinstudio.oktodo.util.getDrawableCompat
import java.text.SimpleDateFormat
import java.util.*

class ProblemFingerprint(context: Context, val onProblemClick: (p: Problem) -> Unit) :
    BaseFingerprint<LayoutProblemBinding, Problem> {

    private val dateFormat by lazy {
        SimpleDateFormat(" dd MMMM yyyy HH:mm", Locale.getDefault())
    }

    private val highImportanceDrawable by lazy {
        context.getDrawableCompat(R.drawable.ic_high_priority)
    }

    private val lowImportanceDrawable by lazy {
        context.getDrawableCompat(R.drawable.ic_low_priority)
    }

    private val doneDrawable by lazy {
        context.getDrawableCompat(R.drawable.ic_problem_done)
    }

    private val textSecondaryColor by lazy {
        context.getColorCompat(R.color.text_secondary)
    }

    private val redPrimaryColor by lazy {
        context.getColorCompat(R.color.red_primary)
    }

    private val untilString by lazy {
        context.getString(R.string.until)
    }

    @LayoutRes
    override fun getLayoutId() = R.layout.layout_problem

    override fun isRelativeItem(item: ListItem) = item is Problem

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

    inner class ProblemViewHolder(binding: LayoutProblemBinding) :
        BaseViewHolder<LayoutProblemBinding, Problem>(binding) {

        private val root = binding.root

        private val tvTitle = binding.tvTitle
        private val tvDeadline = binding.tvDeadline
        private val ivImportance = binding.ivPriority

        override fun onBind(item: Problem) {
            tvTitle.text = item.text

            setImportanceDrawable(item.importance, item.done)
            setDeadlineString(item.deadline, item.done)

            root.setOnClickListener {
                if (!item.done) onProblemClick(item)
            }
        }

        private fun getDeadlineString(deadlineMillis: Long): String {
            val deadlineDate = Date(deadlineMillis * 1000)
            return untilString + dateFormat.format(deadlineDate)
        }

        private fun setDeadlineString(deadlineMillis: Long?, problemDone: Boolean) {
            if (deadlineMillis == null) {
                tvDeadline.visibility = View.GONE

            } else {
                tvDeadline.visibility = View.VISIBLE
                tvDeadline.text = getDeadlineString(deadlineMillis)

                val currentMillis = Calendar.getInstance(Locale.getDefault()).timeInMillis
                if (!problemDone && currentMillis > deadlineMillis * 1000) {
                    tvDeadline.setTextColor(redPrimaryColor)

                } else {
                    tvDeadline.setTextColor(textSecondaryColor)
                }
            }
        }

        private fun setImportanceDrawable(importance: Importance, problemDone: Boolean) {
            if (problemDone) {
                ivImportance.visibility = View.VISIBLE
                ivImportance.setImageDrawable(doneDrawable)

            } else when (importance) {
                Importance.IMPORTANT -> {
                    ivImportance.visibility = View.VISIBLE
                    ivImportance.setImageDrawable(highImportanceDrawable)
                }

                Importance.LOW -> {
                    ivImportance.visibility = View.VISIBLE
                    ivImportance.setImageDrawable(lowImportanceDrawable)
                }

                Importance.BASIC -> {
                    ivImportance.visibility = View.INVISIBLE
                }
            }
        }
    }
}