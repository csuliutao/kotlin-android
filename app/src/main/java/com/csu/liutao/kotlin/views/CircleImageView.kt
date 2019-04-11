package com.csu.liutao.kotlin.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
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
        if (drawable == null) return
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        val layerId =
            canvas!!.saveLayer(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        // 绘制圆形图层
        val dstBmp = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val dstCanvas = Canvas(dstBmp)
        dstCanvas.drawCircle(centerX, centerY, radius, paint)
        canvas!!.drawBitmap(dstBmp, 0F, 0F, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 图片需显示部分左上角移动到裁剪区域左上角，不缩放图片
        val widthD = drawable.bounds.right - drawable.bounds.left
        val heightD = drawable.bounds.bottom - drawable.bounds.top
        val srcBmp = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        srcCanvas.save()
        srcCanvas.translate(- widthD / 2 + radius * dm.density, - heightD / 2 + radius * dm.density)
        drawable.draw(srcCanvas)
        srcCanvas.restore()

        canvas!!.drawBitmap(srcBmp, 0F, 0F, paint)

        paint.xfermode = null
        canvas!!.restoreToCount(layerId)
    }
}