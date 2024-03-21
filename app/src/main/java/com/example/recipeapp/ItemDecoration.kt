package com.example.recipeapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(
    context: Context,
    resId: Int
) : RecyclerView.ItemDecoration() {

    private val divider: Drawable? = ContextCompat.getDrawable(context, resId)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val dividerTop = parent.context.resources.getDimensionPixelSize(R.dimen.main_margin_half)
        val dividerBottom = parent.context.resources.getDimensionPixelSize(R.dimen.main_margin_half)

        for (i in 0 until parent.childCount) {
            if (i != parent.childCount - 1) {
                divider?.setBounds(0, dividerTop, 0, dividerBottom)
                divider?.draw(c)
            }
        }
    }

}