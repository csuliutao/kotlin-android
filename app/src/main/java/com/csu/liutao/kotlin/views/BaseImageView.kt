package com.csu.liutao.kotlin.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

abstract class BaseImageView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ImageView(context, attrs, defStyleAttr) {
    var radius = 0F
    var centerX = 0F
    var centerY = 0F

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            setMeasuredDimension(computeWidth(widthMeasureSpec), computeHeight(heightMeasureSpec))
            compute()
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

    private fun compute() {
        centerX = (measuredWidth.toFloat() - paddingRight + paddingLeft) / 2
        centerY = (measuredHeight.toFloat() - paddingBottom + paddingTop)/ 2
        val x = (measuredWidth.toFloat() - paddingRight - paddingLeft) / 2
        val y = (measuredHeight.toFloat() - paddingBottom - paddingTop)/ 2
        radius = Math.min(x, y)
    }
}