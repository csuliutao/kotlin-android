package com.csu.liutao.cotlin.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView

class CircleImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ImageView(context, attrs, defStyleAttr) {
    var radius = 0F
    var centerX = 0F
    var centerY = 0F
    var paint = Paint()

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        compute()
    }

    private fun compute() {
        val minValue = if (measuredHeight > measuredWidth) measuredWidth else measuredHeight
        radius = minValue.toFloat() / 2
        centerX = measuredWidth.toFloat() / 2
        centerY = measuredHeight.toFloat() / 2
    }

    override fun onDraw(canvas: Canvas?) {
        val layerId =
            canvas!!.saveLayer(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        // 绘制圆形图层
        val dstBmp = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val dstCanvas = Canvas(dstBmp)
        dstCanvas.drawCircle(centerX, centerY, radius, paint)
        canvas!!.drawBitmap(dstBmp, 0F, 0F, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 绘制图片
        val srcBmp = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        drawable.draw(srcCanvas)
        canvas!!.drawBitmap(srcBmp, 0F, 0F, paint)

        paint.xfermode = null
        canvas!!.restoreToCount(layerId)
    }
}