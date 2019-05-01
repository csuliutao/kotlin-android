package com.csu.liutao.kviews

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.widget.OverScroller
import android.widget.Toast
import org.jetbrains.anko.custom.ankoView
import kotlin.math.max
import kotlin.math.min

inline fun ViewManager.scaleImageView(theme: Int = 0, init : ScaleImageView.() -> Unit) : ScaleImageView {
    return ankoView({ScaleImageView(it)}, theme, init)
}
class ScaleImageView(context: Context, attrs:AttributeSet? = null, defStyle : Int = 0) : View(context, attrs, defStyle) {
    var imageResource = R.mipmap.circle
        set(value) {
            field = value
            drawable = resources.getDrawable(value)
            bitmap = toBitmap(drawable)
        }
    var drawable = resources.getDrawable(R.mipmap.circle)
    var bitmap = toBitmap(resources.getDrawable(R.mipmap.circle))

    private var clickX = 0F
    private var clickY = 0F
    private var moveX = 0F
    private var moveY = 0F

    private var canScale = false
    private var isScale = false
    private var scaleRadio = 1.0F
    private var scaleFraction = 0F
        set(value) {
            field = value
            postInvalidate()
        }
    private val animator = ObjectAnimator.ofFloat(this, "scaleFraction", 0F, 1F)

    private var centerX = 0F
    private var centerY = 0F
    private val setMatrix = Matrix()

    private val scroller = OverScroller(context, AccelerateInterpolator())
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener(){
        override fun onDown(e: MotionEvent?): Boolean = true

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            val w = bitmap.width - measuredWidth
            val leftSpace = centerX - bitmap.width / scaleRadio / 2F
            val l = ((clickX - leftSpace) * scaleRadio - clickX).toInt()

            val h = bitmap.height - measuredHeight
            val topSpace = centerY - bitmap.height / scaleRadio / 2F
            val t = ((clickY - topSpace) * scaleRadio - clickY).toInt()

            scroller.fling(moveX.toInt(), moveY.toInt(), velocityX.toInt(), velocityY.toInt(), l - w, l, t - h, t, 100, 100)
            doScroller()
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            if (!canScale) {
                Toast.makeText(context, "already scale ${scaleRadio}", Toast.LENGTH_LONG)
            } else {
                isScale = !isScale
                clickX = e!!.x
                clickY = e!!.y
                modifyClick()
                if (isScale) {
                    animator.start()
                } else {
                    animator.reverse()
                }
            }
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            moveX -= distanceX
            moveY -= distanceY
            modifyMove()
            postInvalidate()
            return true
        }
    })

    private fun doScroller() {
        ViewCompat.postOnAnimation(this, object : Runnable {
            override fun run() {
                while (scroller.computeScrollOffset()){
                    moveX = scroller.currX.toFloat()
                    moveY = scroller.currY.toFloat()
                    postInvalidate()
                    ViewCompat.postOnAnimation(this@ScaleImageView, this)
                }
            }
        })
    }

    private fun modifyMove() {
        val w = bitmap.width - measuredWidth
        val leftSpace = centerX - bitmap.width / scaleRadio / 2F
        val l = (clickX - leftSpace) * scaleRadio - clickX
        moveX = min(moveX, l)
        moveX = max(moveX, l - w)

        val h = bitmap.height - measuredHeight
        val topSpace = centerY - bitmap.height / scaleRadio / 2F
        val t = (clickY - topSpace) * scaleRadio - clickY
        moveY = min(moveY, t)
        moveY = max(moveY, t - h)
    }

    private fun modifyClick() {
        if (canScale) {
            val xLeft = centerX - bitmap.width / scaleRadio / 2F
            val tempS = scaleRadio - 1
            clickX = max(clickX, xLeft * scaleRadio / tempS)
            val w = bitmap.width - measuredWidth
            clickX = min(clickX, (w + xLeft * scaleRadio) / tempS)

            val yLeft = centerY - bitmap.height / scaleRadio / 2F
            clickY = max(clickY, yLeft * scaleRadio / tempS)
            val h = bitmap.height - measuredHeight
            clickY = min(clickY, (h + yLeft * scaleRadio) / tempS)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = bitmap.width + paddingLeft + paddingRight
        val h = bitmap.height + paddingBottom + paddingTop
        var resolveW = resolveSize(w, widthMeasureSpec)
        var resolveH = resolveSize(h, heightMeasureSpec)

        val minScale = min(resolveH.toFloat() / h, resolveW.toFloat() / w)
        resolveW = (w * minScale).toInt()
        resolveH = (h * minScale).toInt()
        setMeasuredDimension(resolveW, resolveH)

        val minW = (measuredWidth.toFloat() - paddingLeft - paddingRight) / bitmap.width
        val minH = (measuredHeight.toFloat() - paddingTop - paddingBottom) / bitmap.height
        val setMinScale = min(minW, minH)

        if (setMinScale < 1) {
            canScale = true
            scaleRadio = 1 / setMinScale
        } else {
            canScale = false
            scaleRadio = setMinScale
        }

        centerX = (measuredWidth + paddingLeft - paddingRight) / 2F
        centerY = (measuredHeight + paddingTop - paddingBottom) / 2F
        clickX = centerX
        clickY = centerY

        val setOffX = centerX - bitmap.width / 2F
        val setOffY = centerY - bitmap.height / 2F
        setMatrix.reset()
        setMatrix.postTranslate(setOffX, setOffY)
        setMatrix.postScale(setMinScale, setMinScale, centerX, centerY)
    }

    override fun onDraw(canvas: Canvas?) {
        val tempM = Matrix(setMatrix)
        val tempS = 1 + (scaleRadio - 1) * scaleFraction
        tempM.postScale(tempS, tempS, clickX, clickY)
        if (!isScale) {
            moveX *= scaleFraction
            moveY *= scaleFraction
        }
        tempM.postTranslate(moveX, moveY)
        canvas!!.drawBitmap(bitmap, tempM, null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}