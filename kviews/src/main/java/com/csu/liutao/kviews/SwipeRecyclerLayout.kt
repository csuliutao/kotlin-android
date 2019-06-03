package com.csu.liutao.kviews

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.TextView
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.sp
import org.jetbrains.anko.textColor
import java.lang.Exception
import kotlin.math.abs

inline fun ViewManager.swipeRecyclerLayout(theme : Int = 0, init : SwipeRecyclerLayout.() -> Unit) : SwipeRecyclerLayout {
    return ankoView({SwipeRecyclerLayout(it)}, theme, init)
}

inline fun ViewManager.recycleView(theme : Int = 0, init : RecyclerView.() -> Unit) : RecyclerView {
    return ankoView({RecyclerView(it)}, theme, init)
}

class SwipeRecyclerLayout(context : Context, attrs : AttributeSet? = null, defStyle : Int = 0) : ViewGroup(context, attrs, defStyle){
    var headerView : View? = null
    var headerId : Int = -1
    var fotterId = -1
    var fotterView : View? = null
    private var curState = NORMAL
    private var curHeight = 0
        set(value) {
            if (value == 0) curState = NORMAL
            field = value
            refreshView()
        }
        get() = field
    private var headerH = dip(32)

    private var downY = 0F

    private var canScroll = true;

    private var recyclerView : RecyclerView? = null

    var listener : OnSwipeListener? = null

    val animator = ObjectAnimator.ofInt(this,"curHeight",0)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        checkChildView()
        recyclerView = getChildAt(0) as RecyclerView
        if (headerView == null) {
            headerView = LayoutInflater.from(context).inflate(headerId, null)
            headerView!!.layoutParams = LayoutParams(matchParent, dip(32))
            if (headerView is TextView) {
                (headerView as TextView).text = "header"
                (headerView as TextView).textSize = sp(6).toFloat()
                (headerView as TextView).setTextColor(Color.RED)
            }
            headerView!!.left = left + paddingLeft
            headerView!!.right = right - paddingRight
            headerView!!.top = 0
            headerView!!.bottom = headerView!!.height
        }
        if (fotterView == null) {
            fotterView = LayoutInflater.from(context).inflate(fotterId, null)
            fotterView!!.layoutParams = LayoutParams(matchParent, dip(32))
            if (fotterView is TextView) {
                (fotterView as TextView).text = "footer"
                (fotterView as TextView).textSize = sp(6).toFloat()
                (fotterView as TextView).setTextColor(Color.RED)
            }
            fotterView!!.left = left + paddingLeft
            fotterView!!.right = right - paddingRight
            fotterView!!.top = bottom - fotterView!!.height
            fotterView!!.bottom = bottom
        }
    }

    private fun checkChildView() {
        if (childCount != 1) {
            curState = INVALID
            throw Exception("not only child recyclerView {$childCount}")
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        when (curState) {
            INVALID -> throw Exception("not only child recyclerView")
            HEADER -> {
                recyclerView?.layout(l + paddingLeft, t + curHeight, r - paddingRight, b)
            }
            FOTTER -> {
                recyclerView?.layout(l + paddingLeft, t, r - paddingRight, b - curHeight)
            }
            else -> recyclerView?.layout(l + paddingLeft, t, r - paddingRight, b)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        when (curState) {
            INVALID -> throw Exception("not only child recyclerView")
            HEADER -> {
                headerView!!.draw(canvas)
            }
            FOTTER -> {
                fotterView!!.draw(canvas)
            }
        }
        super.dispatchDraw(canvas)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        handleTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun handleTouchEvent(ev: MotionEvent?) {
        checkChildView()
        when (ev!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downY = ev!!.y
                if (curState == NORMAL) {
                    canScroll = true
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
                        if (curHeight > headerH) curHeight = headerH
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                canScroll = false
                when (curState) {
                    HEADER -> listener?.onRefresh()
                    FOTTER -> listener?.onLoadMore()
                }
            }

        }
    }

    fun stopRefresh() {
        animator.interpolator = LinearInterpolator()
        animator.duration = 500
        animator.start()
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