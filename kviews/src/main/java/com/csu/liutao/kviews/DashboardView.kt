package com.csu.liutao.kviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import kotlin.math.min

class DashboardView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var graduateLength = dip(13).toFloat()
    private val rectF : RectF = RectF(0F, 0F, 0F, 0F)
    private var sweepAngle = 300F
    var startAngle = 120F
        set(value) {
            field = value % 360
            if (field <= 90 || field >= 270) {
                throw Error("angle is not valid, please check! angle in (2*pi+90, 2*pi+180)")
            }
            sweepAngle = 540 - 2 * field
        }
    var strokeWidth = dip(5).toFloat()
        set(value) {
            field = value
            graduateLength = if (value <= 1) 5 * value else value * 2F + 3F
        }
    var graduateCount = 8
        set(value) {
            if (value <= 1) throw Error("graduate must be > 1")
            field = value
        }
    var color = Color.BLUE
        set(value) {
            field = value
            paint.color = value
        }

    var pointDistance = dip(20).toFloat()

    var pointIndex = 3
        set(value) {
            if (value >= graduateCount) throw Error("index can not > graduateCount")
            field = value
        }

    override fun onDraw(canvas: Canvas?) {
        computeRadius()
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = strokeWidth
        paint.pathEffect = null
        canvas!!.drawArc(rectF, startAngle, sweepAngle, false, paint)


        val tempRectF = RectF(rectF.left + graduateLength / 2, rectF.top + graduateLength / 2,
            rectF.right - graduateLength / 2, rectF.bottom - graduateLength / 2)
        val path = Path()
        path.arcTo(tempRectF, startAngle, sweepAngle)
        val measure = PathMeasure(path, false)
        val eachSpace = (measure.length - strokeWidth) / (graduateCount - 1)
        paint.pathEffect = DashPathEffect(floatArrayOf(strokeWidth, eachSpace - strokeWidth), 0F)
        paint.strokeWidth = graduateLength
        canvas!!.drawPath(path, paint)

        paint.strokeCap = Paint.Cap.ROUND
        paint.pathEffect = null
        paint.strokeWidth = strokeWidth * 2
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas!!.drawPoint(rectF.centerX(), rectF.centerY(), paint)

        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
        val length = tempRectF.width() / 2 - pointDistance
        val radians = Math.toRadians(startAngle + sweepAngle.toDouble() / (graduateCount - 1) * pointIndex)
        val y = length * Math.sin(radians).toFloat()
        val x = length * Math.cos(radians).toFloat()
        canvas!!.drawLine(rectF.centerX(), rectF.centerY(), rectF.centerX() + x, rectF.centerY() + y, paint)
    }

    private fun computeRadius() {
        val centerX = (paddingLeft.toFloat() + measuredWidth - paddingRight)/ 2
        val validH = measuredHeight.toFloat() - paddingBottom - paddingTop
        val sinY = Math.sin(Math.toRadians(startAngle.toDouble()))
        var radius = (validH / ( 1 + sinY)).toFloat()
        val radiusX = (measuredWidth.toFloat() - paddingLeft - paddingRight) / 2
        var centerY = radius + paddingTop
        if (radius > radiusX) {
            radius = radiusX
            val blankSpace = (validH - radius - radius * sinY) / 2
            centerY = paddingTop + blankSpace.toFloat() + radius
        }
        rectF.left = centerX - radius
        rectF.right = centerX + radius
        rectF.top = centerY - radius
        rectF.bottom = centerY + radius
    }

}

inline fun ViewManager.dashboardView(theme : Int = 0, init : DashboardView.() -> Unit) : DashboardView {
    return ankoView({DashboardView(it)}, theme, init)
}