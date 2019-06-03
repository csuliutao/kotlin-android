package com.csu.liutao.kviews

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import android.view.animation.LinearInterpolator
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import java.lang.Exception
import java.lang.Math.abs

inline fun ViewManager.swipeRecyclerLayout(theme : Int = 0, init : SwipeRecyclerLayout.() -> Unit) : SwipeRecyclerLayout {
    return ankoView({SwipeRecyclerLayout(it)}, theme, init)
}

inline fun ViewManager.recycleView(theme : Int = 0, init : RecyclerView.() -> Unit) : RecyclerView {
    return ankoView({RecyclerView(it)}, theme, init)
}

class SwipeRecyclerLayout(context : Context, attrs : AttributeSet? = null, defStyle : Int = 0) : ViewGroup(context, attrs, defStyle){
    var headerView : View? = null
    var headerId : Int = R.layout.test_text_view
    var footerId = R.layout.test_text_view
    var footerView : View? = null
    private var curState = NORMAL
    private var curHeight = 0
        set(value) {
            if (value == 0) curState = NORMAL
            field = value
            refreshView()
        }
        get() = field
    private var headerH = dip(32)

    private var footerH = dip(32)

    private var downY = 0F

    private var canScroll = true;

    private var recyclerView : RecyclerView? = null

    var listener : OnSwipeListener? = null

    val animator = ObjectAnimator.ofInt(this,"curHeight",0)
    val headerAnimator = ObjectAnimator.ofInt(this,"curHeight",headerH)
    val footerAnimator = ObjectAnimator.ofInt(this,"curHeight",footerH)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        checkChildView()
        recyclerView = getChildAt(0) as RecyclerView
        recyclerView!!.overScrollMode = View.OVER_SCROLL_NEVER
        if (headerView == null) {
            headerView = LayoutInflater.from(context).inflate(headerId, null)
            addView(headerView)
        }
        if (footerView == null) {
            footerView = LayoutInflater.from(context).inflate(footerId, null)
            addView(footerView)
        }
    }

    private fun checkChildView() {
        if (childCount != 1) {
            curState = INVALID
            throw Exception("not only child recyclerView {$childCount}")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val newSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST)
        headerView?.measure(widthMeasureSpec, newSpec)
        headerH = headerView!!.measuredHeight
        footerView?.measure(widthMeasureSpec, newSpec)
        footerH = footerView!!.measuredHeight
        recyclerView?.measure(widthMeasureSpec, heightMeasureSpec)
        return setMeasuredDimension(recyclerView!!.measuredWidth, recyclerView!!.measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val distance = if (curState == HEADER) curHeight else -curHeight
        headerView?.layout(l, t - headerH + distance, r, t  + distance)
        footerView?.layout(l, b + distance, r, b + footerH  + distance)
        recyclerView?.layout(l, t + distance, r, b  + distance)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        handleTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun handleTouchEvent(ev: MotionEvent?) {
        when (ev!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downY = ev!!.y
                if (curState == NORMAL) {
                    canScroll = true
                } else {
                    curHeight = 0
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (canScroll) {
                    if (curState != HEADER && curState != FOTTER) {
                        if (isFirstItemShow(ev!!.y)) {
                            curState = HEADER
                            downY = ev!!.y
                        } else if (isLastItemShow(ev!!.y)) {
                            curState = FOTTER
                            downY = ev!!.y
                        }
                    } else {
                        curHeight = abs(ev!!.y - downY).toInt()
                        when (curState) {
                            HEADER -> if (curHeight > headerH) curHeight = headerH
                            FOTTER -> if (curHeight > footerH) curHeight = footerH
                        }

                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                canScroll = false
                when (curState) {
                    HEADER -> {
                        if (curHeight != headerH) startAnimate(headerAnimator, 200)
                        listener?.onRefresh()
                    }
                    FOTTER -> {
                        if (curHeight != footerH) startAnimate(footerAnimator, 200)
                        listener?.onLoadMore()
                    }
                }
            }

        }
    }

    fun stopRefresh() {
        startAnimate(animator)
    }

    private fun startAnimate(anim : ObjectAnimator, duration : Long = 500L) {
        anim.interpolator = LinearInterpolator()
        anim.duration = duration
        anim.start()
    }

    private fun isLastItemShow(y: Float): Boolean {
        if (y > downY) return false
        val manager = recyclerView!!.layoutManager as? LinearLayoutManager
        return manager!!.findLastCompletelyVisibleItemPosition() == manager.itemCount - 1
    }

    private fun isFirstItemShow(y: Float): Boolean {
        if (y < downY) return false
        val manager = recyclerView!!.layoutManager as? LinearLayoutManager
        return manager!!.findFirstCompletelyVisibleItemPosition() == 0
    }

    private fun refreshView() {
        requestLayout()
        invalidate()
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    companion object {
        val INVALID = -1
        val NORMAL = 0;
        val HEADER = 1
        val FOTTER = 2
    }

    interface OnSwipeListener {
        fun onRefresh()
        fun onLoadMore()
    }
}