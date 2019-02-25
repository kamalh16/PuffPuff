package com.base.hamoud.chronictrack.ui.tokelog

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R


class SwipeToDeleteCallback(adapter: TokeLogListAdapter) :
      ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    // to add both left AND right support
    // constructor: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    private val logListAdapter: TokeLogListAdapter = adapter

    private val icon: Drawable?
    private val background: ColorDrawable

    init {
        icon = ContextCompat.getDrawable(
              logListAdapter.context, R.drawable.ic_delete_white_24dp)
        background = ColorDrawable(
              ContextCompat.getColor(adapter.context, R.color.colorSwipeToDeleteBg))
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        // used for up and down movements
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        logListAdapter.deleteItem(position)
    }

    override fun onChildDraw(
          c: Canvas,
          recyclerView: RecyclerView,
          viewHolder: RecyclerView.ViewHolder,
          dX: Float,
          dY: Float,
          actionState: Int,
          isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        when {
//            // Swiping to the right, enable in ItemTouchHelper.SimpleCallback constructor() above
//            dX > 0 -> {
//                val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
//                val iconRight = itemView.left + iconMargin
//                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//
//                background.setBounds(
//                    itemView.left, itemView.top,
//                    itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
//                )
//            }
            // Swiping to the left
            dX < 0 -> {
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                      itemView.right + dX.toInt() - backgroundCornerOffset,
                      itemView.top, itemView.right, itemView.bottom
                )
            }
            else -> // view is unSwiped
                background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }
}