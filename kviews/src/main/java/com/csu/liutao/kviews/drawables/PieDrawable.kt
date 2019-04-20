package com.csu.liutao.kviews.drawables

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

class PieDrawable(val bounds : RectF? = null, val startAngle : Float  = 0F, val sweepAngle: Float = 0F , val paint: Paint = Paint()){
    var isClicked = false
    private var lastDistance : Float = 0F
    private var startAnimator : ValueAnimator = ValueAnimator()
    private var cancleAnimator : ValueAnimator = ValueAnimator()

    fun onDraw(canvas: Canvas) {
        if (bounds == null) return
        val tempBouds = computeBounds(bounds)
        canvas.drawArc(tempBouds, startAngle, sweepAngle, true, paint)
    }

    fun startAnimator(view: View, distance : Float, duration:Long = 1000) {
        if (lastDistance > 0F) {
            cancleAnimator(view, duration)
            return
        }
        startAnimator.setAnimator(view, lastDistance, distance, duration)
        if (cancleAnimator.isRunning) cancleAnimator.cancel()
        if (!startAnimator.isRunning) startAnimator.start()
    }

    fun cancleAnimator (view: View, duration:Long = 1000) {
        if (lastDistance <= 0F) return
        cancleAnimator.setAnimator(view, lastDistance, 0F, duration)
        if (startAnimator.isRunning) startAnimator.cancel()
        if (!cancleAnimator.isRunning) cancleAnimator.start()
    }

    fun ValueAnimator.setAnimator(view: View, start:Float = 0F, end :Float = 0F, duration:Long = 1000) {
        setFloatValues(start, end)
        this.duration = duration
        addUpdateListener {
            lastDistance = it.animatedValue as Float
            view.postInvalidate()
        }
    }

    private fun computeBounds(bounds: RectF): RectF {
        val start = startAngle % 360
        val centerAngle = (start + sweepAngle / 2).toDouble()
        val radians = Math.toRadians(centerAngle)
        var h = lastDistance * Math.sin(radians).toFloat()
        var w = lastDistance * Math.cos(radians).toFloat()
        return RectF(bounds.left + w,bounds.top + h, bounds.right + w, bounds.bottom + h)
    }

    fun isInBound (event: MotionEvent?) : Boolean {
        if (event == null) return false
        val start = startAngle % 360
        val end : Float = start + sweepAngle
        val x = event.x - bounds!!.centerX()
        val y = event.y - bounds!!.centerY()
        var angle = Math.toDegrees(Math.atan(y.toDouble() / x))
        if (x < 0) angle = angle + 180
        if (angle < 0) angle += 360

        return angle >= start && angle <= end
    }
}