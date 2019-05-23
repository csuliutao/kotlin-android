package com.csu.liutao.kviews

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import android.view.animation.LinearInterpolator
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.padding
import org.jetbrains.anko.toast

class FloatCircleWindow (val mContext : Context, val mCircle : Int = 200){
    private val mView = CircleImageView(mContext)
    private val mWindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val mParams = WindowManager.LayoutParams()
    private var mIsShow = false

    private var curAnimator = ObjectAnimator.ofFloat(mView, "rotation", 0F, 360F)

    private val mListener = object : View.OnTouchListener {
        var mIsMoved = false
        var lastX = 0F
        var lastY = 0F
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when(event?.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    mIsMoved = false
                    lastX = event.rawX
                    lastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    mIsMoved = true
                    mParams.x += (event.rawX - lastX).toInt()
                    lastX = event.rawX
                    mParams.y += (event.rawY - lastY).toInt()
                    lastY = event.rawY
                    mWindowManager.updateViewLayout(mView, mParams)
                }
                MotionEvent.ACTION_UP -> {
                    if (!mIsMoved) {
                        mContext.toast("click me")
                        if (curAnimator.isRunning) curAnimator.cancel() else curAnimator.start()
                    }
                }
            }
            return true
        }
    }

    init {
        mParams.width = mCircle
        mParams.height = mCircle
        mParams.type = WindowManager.LayoutParams.LAST_SUB_WINDOW
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        mParams.x = mCircle
        mParams.y = mCircle
        mParams.alpha = 1F
        mParams.format = PixelFormat.TRANSLUCENT

        mView.imageResource = R.mipmap.circle
        mView.layoutParams = ViewGroup.LayoutParams(mCircle, mCircle)
        mView.padding = mContext.dip(5)
        mView.setOnTouchListener(mListener)

        curAnimator.repeatCount = ValueAnimator.INFINITE
        curAnimator.interpolator = LinearInterpolator()
        curAnimator.duration = 6000
    }

    public fun show() : Unit{
        if (!mIsShow) {
            mIsShow = true
            mWindowManager.addView(mView, mParams)
            curAnimator.start()
        }
    }

    public fun hidden() : Unit {
        if (mIsShow) {
            mIsShow = false
            mWindowManager.removeView(mView)
            curAnimator.cancel()
        }
    }

    public fun destory() : Unit {
        hidden()
    }
}