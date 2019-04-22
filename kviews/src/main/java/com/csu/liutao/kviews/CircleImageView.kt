package com.csu.liutao.kviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import android.widget.ImageView
import com.csu.liutao.kviews.drawables.CircleDrawable
import com.csu.liutao.kviews.drawables.CircleImageShaderDrawable
import org.jetbrains.anko.custom.ankoView

class CircleImageView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ImageView(context, attrs, defStyleAttr) {
    var circleDrawable: CircleDrawable = CircleImageShaderDrawable()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            val w = drawable.intrinsicWidth + paddingLeft + paddingRight
            val h = drawable.intrinsicHeight + paddingBottom + paddingTop
            setMeasuredDimension(resolveSize(w, widthMeasureSpec), resolveSize(h, heightMeasureSpec))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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