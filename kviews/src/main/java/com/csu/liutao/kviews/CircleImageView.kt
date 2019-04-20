package com.csu.liutao.kviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.ImageView
import com.csu.liutao.kviews.drawables.CircleDrawable
import com.csu.liutao.kviews.drawables.CircleShaderDrawable
import org.jetbrains.anko.custom.ankoView

class CircleImageView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ImageView(context, attrs, defStyleAttr) {
    var radius = 0F
    var centerX = 0F
    var centerY = 0F
    var circleDrawable: CircleDrawable = CircleShaderDrawable()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            setMeasuredDimension(computeWidth(widthMeasureSpec), computeHeight(heightMeasureSpec))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    private fun computeHeight(heightMeasureSpec: Int): Int {
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val m = MeasureSpec.getMode(heightMeasureSpec)
        when (m) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY -> return h
            MeasureSpec.AT_MOST -> return Math.min(h, drawable.intrinsicHeight + paddingBottom + paddingTop)
            else -> return 0
        }
    }

    private fun computeWidth(widthMeasureSpec: Int): Int {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val m = MeasureSpec.getMode(widthMeasureSpec)
        when (m) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY -> return w
            MeasureSpec.AT_MOST -> return Math.min(w, drawable.intrinsicWidth + paddingLeft + paddingRight)
            else -> return 0
        }
    }

    override fun onDraw(canvas: Canvas?) {
        circleDrawable.bounds = Rect(paddingLeft, paddingTop, measuredWidth - paddingRight, measuredHeight - paddingBottom)
        circleDrawable.drawable = drawable
        circleDrawable.draw(canvas!!)
    }
}

inline fun ViewManager.circleImageView(theme: Int = 0, init: CircleImageView.() -> Unit): CircleImageView {
    return ankoView({ CircleImageView(it) }, theme, init)
}