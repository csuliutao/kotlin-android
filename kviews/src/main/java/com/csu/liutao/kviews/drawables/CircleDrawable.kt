package com.csu.liutao.kviews.drawables

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import kotlin.math.min

abstract class CircleDrawable{
    protected var radius = 0F
    protected var centerX = 0F
    protected var centerY = 0F
    var bounds : Rect? = null
        set(value) {
            field = value
            if (value != null) {
                centerX = value!!.exactCenterX()
                centerY = value!!.exactCenterY()
                radius = min(value.width(), value.height()).toFloat() / 2
            }
        }
    var drawable : Drawable? = null
        set(value) { field = value}

    abstract fun draw(canvas: Canvas)
}