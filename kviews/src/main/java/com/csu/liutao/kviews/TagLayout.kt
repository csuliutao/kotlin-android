package com.csu.liutao.kviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import kotlin.math.max

inline fun ViewManager.tagLayout(theme: Int = 0, init : TagLayout.() ->Unit) :TagLayout {
    return ankoView({TagLayout(it)}, theme, init)
}

class TagLayout(context : Context, attrs : AttributeSet? = null, defStyle : Int = 0) : ViewGroup(context, attrs, defStyle) {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var i =0;
        while (i < childCount) {
            val child = getChildAt(i)
            child.layout(child.left, child.top, child.right, child.bottom)
            i++;
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthLineUsed = paddingLeft
        var heightLineUsed = 0
        var widthUsed = 0
        var heightUsed = 0
        var wMode = MeasureSpec.getMode(widthMeasureSpec)
        var w = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        w = if (w < 0) 0 else w

        var i =0;
        while (i < childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val childMargin = child.layoutParams as MarginLayoutParams
            if ((wMode != MeasureSpec.UNSPECIFIED) && (widthLineUsed + child.measuredWidth + childMargin.leftMargin + childMargin.rightMargin) > w) {
                heightUsed += heightLineUsed
                widthLineUsed = paddingLeft
                widthUsed = max(widthLineUsed, widthUsed)
            }
            child.left = widthLineUsed + childMargin.leftMargin
            child.right = child.left + child.measuredWidth
            child.top = heightUsed + childMargin.topMargin
            child.bottom = child.top + child.measuredHeight

            widthLineUsed += child.measuredWidth + childMargin.leftMargin + childMargin.rightMargin
            heightLineUsed = max(heightLineUsed, child.measuredHeight + childMargin.topMargin + childMargin.bottomMargin)
            i++;
        }
        if (widthLineUsed != paddingLeft) {
            heightUsed += heightLineUsed
            widthUsed = max(widthLineUsed, widthUsed)
        }
        setMeasuredDimension(resolveSize(widthUsed, widthMeasureSpec), View.resolveSize(heightUsed, heightMeasureSpec))
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }


}