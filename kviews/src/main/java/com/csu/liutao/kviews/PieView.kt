package com.csu.liutao.kviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import com.csu.liutao.kviews.drawables.PieDrawable
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import java.lang.Error
import kotlin.math.min

class PieView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    var animatorDisatance :  Float = dip(10).toFloat();
    var startAngle :Float = 0F
    var pieDatas = mutableMapOf<Float,Int>()
        set(value) {
            if (value == null || value.isEmpty()) {
                throw Error("pie data error, please check")
            }
            if (value.keys.sum() < 1) {
                throw Error("pie weight error :"+value.keys.sum())
            }
            field.clear()
            field.putAll(value)
        }
    var hasClickAnimator = true
    var isCirclePie = true
    private var pieDrawables = mutableMapOf<Float,PieDrawable>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lastDrawable : PieDrawable? = null


    override fun onDraw(canvas: Canvas?) {
        val pieBounds = getBounds()
        var start = startAngle
        val isEmpty = pieDrawables.isEmpty()
        for (key in pieDatas.keys) {
            paint.color = pieDatas.get(key) ?: Color.RED
            if (isEmpty) {
                val pieDrawable = PieDrawable(pieBounds, start, 360 * key, paint)
                pieDrawables.put(key,pieDrawable)
            }
            pieDrawables.get(key)!!.onDraw(canvas!!)
            start += 360 * key
        }
    }

    private fun getBounds(): RectF {
        var l = paddingLeft + animatorDisatance;
        val r = width - paddingRight - animatorDisatance
        val t = paddingTop + animatorDisatance
        val b =  height - paddingBottom - animatorDisatance
        val rectF = RectF(l, t, r, b)
        if (!isCirclePie) return rectF else {
            val radius = min(rectF.width(), rectF.height()) / 2
            return RectF(rectF.centerX() - radius, rectF.centerY() - radius, rectF.centerX() + radius, rectF.centerY() + radius)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        if (!hasClickAnimator || event == null) return true
        if (lastDrawable != null) {
            lastDrawable!!.isClicked = false
            lastDrawable!!.cancleAnimator(this)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                for (drawable in pieDrawables.values) {
                    if (drawable.isInBound(event)) {
                        drawable.isClicked = true
                        lastDrawable = drawable
                        break;
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                lastDrawable!!.startAnimator(this, animatorDisatance + min(min(paddingBottom, paddingRight), min(paddingRight, paddingLeft)))
            }
        }
        return true
    }
}

inline fun ViewManager.pieView(theme :Int = 0, init : PieView.() -> Unit) :PieView {
    return ankoView({PieView(it)}, theme, init)
}