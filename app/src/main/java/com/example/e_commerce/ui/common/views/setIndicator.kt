package com.example.e_commerce.ui.common.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.e_commerce.R

fun setIndicator(count: Int, context: Context,indicatorView: LinearLayout ) {
    indicatorView.removeAllViews()
        for (i in 0 until count) {
            val dot = View(context).apply {
                layoutParams = LinearLayout.LayoutParams(20, 20).apply {
                    marginStart = 8
                    marginEnd = 8
                }
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.dot_unselected
                )
            }
           indicatorView.addView(dot)
        }
}

fun updateIndicator(position: Int,indicatorView: LinearLayout, context: Context ) {
    for (i in 0 until indicatorView.childCount) {
        val dot = indicatorView.getChildAt(i)
        dot.background = if (i == position) {
            ContextCompat.getDrawable(context, R.drawable.dot_selected)
        } else {
            ContextCompat.getDrawable(context, R.drawable.dot_unselected)
        }
    }
}