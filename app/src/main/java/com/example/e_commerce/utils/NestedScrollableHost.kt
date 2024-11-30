package com.example.e_commerce.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import kotlin.math.absoluteValue
import kotlin.math.sign

class NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> child?.canScrollHorizontally(direction) ?: false
            1 -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }
private fun handleInterceptTouchEvent(e: MotionEvent) {
    val orientation = parentViewPager?.orientation ?: return

    // Early return if the child can't scroll in the same direction as the parent
    if (!canChildScrollInEitherDirection(orientation)) return

    when (e.action) {
        MotionEvent.ACTION_DOWN -> {
            initializeTouch(e)
        }
        MotionEvent.ACTION_MOVE -> {
            val (dx, dy) = e.x - initialX to e.y - initialY
            handleMoveEvent(dx, dy, orientation)
        }
    }
}

    private fun canChildScrollInEitherDirection(orientation: Int): Boolean {
        return canChildScroll(orientation, -1f) || canChildScroll(orientation, 1f)
    }

    private fun initializeTouch(e: MotionEvent) {
        initialX = e.x
        initialY = e.y
        parent.requestDisallowInterceptTouchEvent(true)
    }

    private fun handleMoveEvent(dx: Float, dy: Float, orientation: Int) {
        val isVpHorizontal = orientation == ORIENTATION_HORIZONTAL
        val scaledDx = dx.absoluteValue * if (isVpHorizontal) 0.5f else 1f
        val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else 0.5f

        if (scaledDx > touchSlop || scaledDy > touchSlop) {
            handleParentInterception(isVpHorizontal, scaledDx, scaledDy, dx, dy, orientation)
        }
    }

    private fun handleParentInterception(
        isVpHorizontal: Boolean, scaledDx: Float, scaledDy: Float, dx: Float, dy: Float, orientation: Int
    ) {
        if (isPerpendicularGesture(isVpHorizontal, scaledDx, scaledDy)) {
            parent.requestDisallowInterceptTouchEvent(false)
        } else {
            val canScroll = canChildScroll(orientation, if (isVpHorizontal) dx else dy)
            parent.requestDisallowInterceptTouchEvent(canScroll)
        }
    }

    private fun isPerpendicularGesture(isVpHorizontal: Boolean, scaledDx: Float, scaledDy: Float): Boolean {
        return isVpHorizontal == (scaledDy > scaledDx)
    }

}