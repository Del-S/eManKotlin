package utils

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Used to display dividing line in RecyclerView for devices
 */
class SimpleDividerItemDecoration(// Divider drawable
        private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        // Get dimensions of RecyclerView
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        // Count how many rows is in the RecyclerView
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            // Get View of child on specific position in RecyclerView
            val child = parent.getChildAt(i)

            // Gets layout information of child
            val params = child.layoutParams as RecyclerView.LayoutParams

            // Calculate position of the line for teh display
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            // Draw line divider
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        // Add size of divider to the bottom of the child to make it visible
        outRect.set(0, 0, 0, mDivider.intrinsicWidth)
    }
}