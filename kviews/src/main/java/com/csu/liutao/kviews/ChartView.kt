package com.csu.liutao.kviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

inline fun ViewManager.chartView(theme : Int = 0, init : ChartView.() -> Unit) : ChartView {
    return ankoView({ChartView(it)},theme, init)
}

class ChartView(context: Context, attrs : AttributeSet? = null, defStyle : Int = 0) : View(context, attrs, defStyle){
    private val xList = mutableListOf<String>()
    private val yList = mutableListOf<Int>()

    private val xAxisList = mutableListOf<String>()
    private val yAxisList = mutableListOf<Int>()

    private val yPosList = mutableListOf<Float>()
    private val yValuePosList = mutableListOf<Float>()
    private val xValuePosList = mutableListOf<Float>()

    private var xMaxCount = DEFAULT_SPACE_NUM
        set(value) {
            field = if (value < DEFAULT_SPACE_NUM) DEFAULT_SPACE_NUM else value
        }
    private var yCount = DEFAULT_SPACE_NUM
        set(value) {
            field = if (value < DEFAULT_SPACE_NUM) DEFAULT_SPACE_NUM else value
        }

    var title :String? = "test chart"
        set(value) {
            field = value
            titleDrawable.name = value
        }
    var xUnit :String? = "xunit"
        set(value) {
            field = value
            xUnitDrawable.name = value
        }
    var yUnit :String? = "yunit"
        set(value) {
            field = value
            yUnitDrawable.name = value
        }

    var yWidth = dip(PADDING_WIDTH)
    var xHeight = dip(PADDING_WIDTH)

    var valueDrawable : AxisDrawable<Float, Float> = ValueDrawable()
    var xAxisDrawable : AxisDrawable<Float, String> = XAxisDrawable()
    var yAxisDrawable : AxisDrawable<Float, Int> = YAxisDrawable()

    var xUnitDrawable : StringDrawable = XStringDrawable(xUnit)
    var yUnitDrawable : StringDrawable = YStringDrawable(yUnit)
    var titleDrawable : StringDrawable = XStringDrawable(title)

    private var xAxisRect = RectF()
    private var yAxisRect = RectF()
    private var xUnitRect = RectF()
    private var yUnitRect = RectF()
    private var titleRect = RectF()

    private var dataRect = RectF()

    private var moveDis = 0F
    private var leftMoveDis = 0F
    private var eachSpace = 0F

    init {
        titleDrawable.paint.textSize = context.sp(16).toFloat()
        xUnitDrawable.paint.textSize = context.sp(10).toFloat()
        yUnitDrawable.paint.textSize = context.sp(10).toFloat()
        valueDrawable.paint.textSize = context.sp(10).toFloat()
        xAxisDrawable.paint.textSize = context.sp(10).toFloat()
        xAxisDrawable.paint.textSize = context.sp(10).toFloat()
    }

    private val gestureDetector = GestureDetector(object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return xList.size > (xMaxCount + 1)
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            val dis = e2!!.x - e1!!.x
            if (isCanScroll(dis < 0)) {
                moveDis += dis
                caculateXList()
                invalidate()
            }
            return true
        }
    })

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

        yAxisRect.left = 0F
        yAxisRect.right = yWidth.toFloat()
        yAxisRect.top = xHeight.toFloat()
        yAxisRect.bottom = measuredHeight.toFloat() - xHeight

        yUnitRect.left = 0F
        yUnitRect.right = yWidth.toFloat()
        yUnitRect.top = 0F
        yUnitRect.bottom = xHeight.toFloat()


        xAxisRect.left = yWidth.toFloat()
        xAxisRect.right = measuredWidth.toFloat() - yWidth
        xAxisRect.top = measuredHeight.toFloat() - xHeight
        xAxisRect.bottom = measuredHeight.toFloat()

        xUnitRect.left = measuredWidth.toFloat() - yWidth
        xUnitRect.right = measuredWidth.toFloat()
        xUnitRect.top = measuredHeight.toFloat() - xHeight
        xUnitRect.bottom = measuredHeight.toFloat()

        eachSpace = (measuredWidth - yWidth * 2).toFloat() / xMaxCount
        leftMoveDis = if(xList.size <= xMaxCount) 0F else (xList.size - xMaxCount) * eachSpace

        caculateYList()

        caculateXList()

        xAxisDrawable.setPosList(xValuePosList)
        xAxisDrawable.setValueList(xAxisList)

        yAxisDrawable.setPosList(yPosList)
        yAxisDrawable.setValueList(yAxisList)

        valueDrawable.setPosList(xValuePosList)
        valueDrawable.setValueList(yValuePosList)
    }

    override fun onDraw(canvas: Canvas?) {
        xUnitDrawable.draw(xUnitRect, canvas)
        yUnitDrawable.draw(yUnitRect, canvas)
        titleDrawable.draw(titleRect, canvas)
        xAxisDrawable.draw(xAxisRect, canvas)
        valueDrawable.draw(dataRect, canvas)
        yAxisDrawable.draw(yAxisRect, canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun isCanScroll(isRight : Boolean = false) : Boolean {
        if (isRight) {
            return moveDis >= 0
        } else {
            return moveDis <= leftMoveDis
        }
    }

    fun setChartData(xValues : MutableList<String>, yValues : MutableList<Int>, xNum : Int = DEFAULT_SPACE_NUM, yNum : Int = DEFAULT_SPACE_NUM) {
        xList.clear()
        xList.addAll(xValues)

        yList.clear()
        yList.addAll(yValues)

        xMaxCount = xNum
        yCount = yNum
    }

    private fun caculateXList() {
        val yEachSpace = dataRect.height() / (yAxisList.get(yCount) - yAxisList.get(0))

        val eachSpace = dataRect.width() / xMaxCount
        val movePos = (moveDis / eachSpace).toInt()
        var index = xList.size - 1 - movePos
        var num = -1;
        xAxisList.clear()
        xValuePosList.clear()
        while (index > 0 && num < xMaxCount) {
            xAxisList.add(xList.get(index))
            xValuePosList.add(dataRect.right - (xList.size - 1 - index) * eachSpace + moveDis)
            yValuePosList.add(dataRect.bottom - (yList.get(index) - yAxisList.get(0)) * yEachSpace)
            index--
            num++
        }
    }

    fun show() {
        requestLayout()
        invalidate()
    }

    private fun caculateYList() {
        var min = 0
        var max =0
        for (value in yList) {
            if (min > value) min = value
            if (max < value) max = value
        }

        val minR = min!! / yCount
        min = (minR - 1) * yCount

        val maxR = max!! / yCount
        max = (maxR + 1) * yCount

        val eachSpace = (max - min) / yCount

        yAxisList.clear()
        yPosList.clear()
        var i = 0
        val disRatio = dataRect.height() / (max - min)
        while (i <= yCount) {
            yAxisList.add(min + i * eachSpace)
            yPosList.add(dataRect.bottom - disRatio * eachSpace * i)
            i++
        }
    }


    companion object{
        val DEFAULT_SPACE_NUM = 5
        val PADDING_WIDTH = 32
    }


    interface RectDrawable {
        fun draw(rect: RectF, canvas: Canvas?)
    }

    abstract class StringDrawable(var name: String?) : RectDrawable{
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    class VerticalDrawable(val xAxisDrawable : RectDrawable){
        fun translateXAxisRectFromY(rect: RectF) : RectF {
            val centerX = rect.centerX()
            val centerY = rect.centerY()
            val width = rect.width() / 2
            val heigh = rect.height() / 2
            return RectF(centerX - heigh, centerY - width, centerX + heigh, centerY + width)
        }

        fun draw(rect: RectF, canvas: Canvas?) {
            val newRect = translateXAxisRectFromY(rect)
            canvas!!.save()
            canvas!!.rotate(-90F, rect.centerX(), rect.centerY())
            xAxisDrawable.draw(newRect, canvas)
            canvas!!.rotate(90F, rect.centerX(), rect.centerY())
            canvas!!.restore()
        }
    }

    class XStringDrawable(name :String?) : StringDrawable(name){
        override fun draw(rect: RectF, canvas: Canvas?) {
            if (TextUtils.isEmpty(name)) return

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

    class YStringDrawable(name :String?) : StringDrawable(name){
        var drawable : VerticalDrawable? = null
        init {
            drawable = VerticalDrawable(XStringDrawable(name))
        }
        override fun draw(rect: RectF, canvas: Canvas?) {
            drawable!!.draw(rect, canvas)
        }
    }

    abstract class AxisDrawable<T, U>() : RectDrawable {
        val posList : MutableList<T> = mutableListOf()
        val valueList : MutableList<U> = mutableListOf()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun setPosList(list : MutableList<T>) {
            posList.clear()
            posList.addAll(list)
        }

        fun setValueList(list : MutableList<U>) {
            valueList.clear()
            valueList.addAll(list)
        }
    }

    class YAxisDrawable() : AxisDrawable<Float, Int>() {
        override fun draw(rect: RectF, canvas: Canvas?) {
            canvas!!.drawLine(rect.right, rect.top, rect.right, rect.bottom, paint)
            val lineWidth = rect.width() / 4
            var index = 0;
            var maxIndex = posList.size - 2
            val drawable = XStringDrawable(valueList.get(0).toString())
            drawable.paint.textSize = paint.textSize
            val rectF = RectF()
            rectF.left = rect.left + lineWidth
            rectF.right = rect.right - lineWidth
            val eachSpace = posList.get(1) - posList.get(0)

            while (index <= maxIndex) {
                canvas!!.drawLine(rect.right, posList.get(index), rect.right - lineWidth, posList.get(index), paint)
                rectF.bottom = posList.get(index) - eachSpace / 4
                rectF.top = posList.get(index + 1) - eachSpace / 4
                drawable.name = valueList.get(index).toString()
                drawable.draw(rectF, canvas)
                index++
            }
        }
    }

    class XAxisDrawable() : AxisDrawable<Float, String>() {
        override fun draw(rect: RectF, canvas: Canvas?) {
            canvas!!.save()
            canvas.clipRect(rect)
            canvas!!.drawLine(rect.left, rect.top, rect.right, rect.top, paint)
            val lineWidth = rect.height() / 4
            var index = 0;
            var maxIndex = posList.size - 1
            val drawable = XStringDrawable(valueList.get(0))
            drawable.paint.textSize = paint.textSize
            val rectF = RectF()
            rectF.top = rect.top + lineWidth
            rectF.bottom = rect.bottom - lineWidth
            val eachSpace = posList.get(1) - posList.get(0)

            while (index <= maxIndex) {
                canvas!!.drawLine(posList.get(index), rect.top, posList.get(index), rect.top + lineWidth, paint)
                rectF.left = posList.get(index) + eachSpace * 3 / 4
                rectF.right = posList.get(index) - eachSpace / 4
                drawable.name = valueList.get(index)
                drawable.draw(rectF, canvas)
                index++
            }
            canvas!!.restore()
        }
    }

    class ValueDrawable () : AxisDrawable<Float, Float>() {
        override fun draw(rect: RectF, canvas: Canvas?) {
            canvas!!.save()
            canvas.clipRect(rect)

            canvas!!.restore()
        }
    }
}