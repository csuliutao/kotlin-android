package com.csu.liutao.kviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

inline fun ViewManager.chartView(theme : Int = 0, init : ChartView.() -> Unit) : ChartView {
    return ankoView({ChartView(it)},theme, init)
}

class ChartView(context: Context, attrs : AttributeSet? = null, defStyle : Int = 0) : View(context, attrs, defStyle){
    val xList = mutableListOf<String>()
    val yList = mutableListOf<Int>()

    var xMaxCount = DEFAULT_SPACE_NUM
        set(value) {
            field = if (value < DEFAULT_SPACE_NUM) DEFAULT_SPACE_NUM else value
        }
    var yCount = DEFAULT_SPACE_NUM
        set(value) {
            field = if (value < DEFAULT_SPACE_NUM) DEFAULT_SPACE_NUM else value
        }

    var title :String? = "test chart"
    var xUnit :String? = "unit"
    var yUnit :String? = "unit"

    var yWidth = dip(PADDING_WIDTH)
    var xHeight = dip(PADDING_WIDTH)

    var itemClickListener : OnItemClickListener? = null

    var xAxisDrawable : RectDrawable? = null
    var yAxisDrawable : RectDrawable? = null

    var xUnitDrawable : RectDrawable = VerticalStringDrawable(xUnit)
    var yUnitDrawable : RectDrawable = StringDrawable(yUnit)
    var titleDrawable : RectDrawable = StringDrawable(title)

    private var xAxisRect = RectF()
    private var yAxisRect = RectF()
    private var xUnitRect = RectF()
    private var yUnitRect = RectF()
    private var titleRect = RectF()

    private var dataRect = RectF()

    private var moveDis = 0F
    private var leftBounds = 0F
    private var eachSpace = 0F

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(w, h)

        dataRect.left = yWidth.toFloat()
        dataRect.right = measuredWidth.toFloat() - yWidth
        dataRect.top = xHeight.toFloat()
        dataRect.bottom = measuredHeight.toFloat() - xHeight

        titleRect.left = yWidth.toFloat()
        titleRect.right = measuredWidth.toFloat() - yWidth
        titleRect.top = 0F
        titleRect.bottom = xHeight.toFloat()

        xAxisRect.left = yWidth / 2F
        xAxisRect.right = yWidth.toFloat()
        xAxisRect.top = xHeight.toFloat()
        xAxisRect.bottom = measuredHeight.toFloat() - xHeight

        xUnitRect.left = 0F
        xUnitRect.right = yWidth.toFloat()
        xUnitRect.top = 0F
        xUnitRect.bottom = xHeight.toFloat()

        yAxisRect.left = yWidth.toFloat()
        yAxisRect.right = measuredWidth.toFloat() - yWidth
        yAxisRect.top = measuredHeight.toFloat() - xHeight
        yAxisRect.bottom = measuredHeight - xHeight / 2F

        yUnitRect.left = measuredWidth.toFloat() - yWidth
        yUnitRect.right = measuredWidth.toFloat()
        yUnitRect.top = measuredHeight.toFloat() - xHeight
        yUnitRect.bottom = measuredHeight.toFloat()

        eachSpace = (measuredWidth - yWidth * 2).toFloat() / xMaxCount
        leftBounds = if(xList.size <= xMaxCount) 0F else -(xList.size - xMaxCount) * eachSpace
    }

    override fun onDraw(canvas: Canvas?) {
        drawAxis(canvas)
        drawVaule(canvas)
    }

    private fun drawVaule(canvas: Canvas?) {

    }

    private fun drawAxis(canvas: Canvas?) {
        xUnitDrawable.draw(xUnitRect, canvas)
        yUnitDrawable.draw(yUnitRect, canvas)
        titleDrawable.draw(titleRect, canvas)
    }

    fun refreshView() {
        requestLayout()
        invalidate()
    }

    private fun isCanScroll() : Boolean {
        return moveDis <= leftBounds
    }


    companion object{
        val DEFAULT_SPACE_NUM = 5
        val PADDING_WIDTH = 32
    }

    open inner class StringDrawable(val name :String?) : RectDrawable{
        override fun draw(rect: RectF, canvas: Canvas?) {
            if (TextUtils.isEmpty(name)) return
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = context.sp(PADDING_WIDTH).toFloat()

            var measureRect = Rect()
            paint.getTextBounds(name, 0, name!!.length, measureRect)

            var width = getModifyTextBounds(measureRect.width(), paint.textSize)
            while (width > rect.width() || measureRect.height() > rect.height()) {
                paint.textSize = paint.textSize - 1
                paint.getTextBounds(name, 0, name!!.length, measureRect)
                width = getModifyTextBounds(measureRect.width(), paint.textSize)
            }
            var metrics = Paint.FontMetrics()
            paint.getFontMetrics(metrics)
            val startX = rect.left + (rect.width() - width) / 2
            val startY = rect.bottom - (rect.height() - measureRect.height()) / 2
            canvas!!.drawText(name, startX, startY, paint)
        }

        private fun getModifyTextBounds(width : Int, modify : Float) : Float {
            return width + modify / 2
        }
    }

    inner class VerticalStringDrawable(name :String?) : StringDrawable(name){
        override fun draw(rect: RectF, canvas: Canvas?) {
            if (TextUtils.isEmpty(name)) return
            val centerX = rect.centerX()
            val centerY = rect.centerY()
            val width = rect.width() / 2
            val heigh = rect.height() / 2

            val newRect = RectF(centerX - heigh, centerY - width, centerX + heigh, centerY + width)
            canvas!!.save()
            canvas!!.rotate(-90F, centerX, centerY)
            super.draw(newRect, canvas)
            canvas!!.rotate(90F, centerX, centerY)
            canvas!!.restore()
        }
    }

    data class ValueInfo(val xValue : String,val yValue : Int, val rect: RectF, val pos : Int)

    interface RectDrawable {
        fun draw(rect: RectF, canvas: Canvas?)
    }

    interface ValueDrawable {
        fun draw(rect: RectF,canvas: Canvas?,preInfo : ValueInfo?, curInfo : ValueInfo, nextInfo : ValueInfo?)
    }

    interface OnItemClickListener {
        fun itemClick(rect: RectF,canvas: Canvas?, curInfo : ValueInfo)
    }
}