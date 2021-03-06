package com.weinstudio.oktodo.ui.main.adapter.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.util.getDrawableCompat

class ItemSwipeCallback(
    private val onItemDelete: (Int) -> Unit,
    private val onItemDone: (Int) -> Unit
) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.ACTION_STATE_IDLE,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

    private val background = ColorDrawable()

    private val doneBackground = Color.parseColor("#559858")
    private val deleteBackground = Color.parseColor("#C54540")

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder

    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        when (direction) {
            ItemTouchHelper.LEFT -> {
                onItemDelete(position)
            }

            ItemTouchHelper.RIGHT -> {
                onItemDone(position)
            }
        }
    }

    lateinit var context: Context

    private val deleteResourceDrawable by lazy {
        context.getDrawableCompat(R.drawable.ic_swipe_delete)
    }

    private val doneResourceDrawable by lazy {
        context.getDrawableCompat(R.drawable.ic_done_28dp)
    }

    override fun onChildDraw(
        c: Canvas, rv: RecyclerView, holder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, aState: Int, isActive: Boolean
    ) {

        context = rv.context

        val intrinsicWidth = deleteResourceDrawable.intrinsicWidth
        val intrinsicHeight = doneResourceDrawable.intrinsicHeight

        val itemView = holder.itemView
        val itemHeight = itemView.bottom - itemView.top

        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconBottom = iconTop + intrinsicHeight
        val (iconLeft, iconRight) = getIconPositionHorizontal(
            itemView,
            iconMargin,
            dX,
            intrinsicWidth
        )

        if (dX > 0) {
            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )

            background.color = doneBackground
            background.draw(c)

            doneResourceDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            doneResourceDrawable.draw(c)

        } else if (dX < 0) {
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.color = deleteBackground
            background.draw(c)

            deleteResourceDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            deleteResourceDrawable.draw(c)
        }

        super.onChildDraw(c, rv, holder, dX, dY, aState, isActive)
    }

    private fun getIconPositionHorizontal(iv: View, im: Int, dX: Float, iw: Int): Pair<Int, Int> {
        val iconLeft: Int
        val iconRight: Int

        if (dX > 0) {
            iconLeft = iv.left + im
            iconRight = iv.left + im + iw

        } else {
            iconLeft = iv.right - im - iw
            iconRight = iv.right - im
        }

        return Pair(iconLeft, iconRight)
    }
}