package com.csu.liutao.kotlin.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.ImageView
import org.jetbrains.anko.custom.ankoView

class CircleImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ImageView(context, attrs, defStyleAttr) {
    var radius = 0F
    var centerX = 0F
    var centerY = 0F

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
        val paint = Paint()
        paint.isAntiAlias = true


        val layerId =
            canvas!!.saveLayer(centerX - radius, centerY - radius, centerX + radius, centerY + radius, paint)
        // 绘制圆形图层
        val dstBmp = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val dstCanvas = Canvas(dstBmp)
        dstCanvas.drawCircle(centerX, centerY, radius, paint)
        canvas!!.drawBitmap(dstBmp, 0F, 0F, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 图片需显示部分左上角移动到裁剪区域左上角，不缩放图片
        val srcBmp = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        drawable.draw(srcCanvas)

        val temMatrix = Matrix()
        temMatrix.postTranslate(centerX - drawable.intrinsicWidth / 2, centerY - drawable.intrinsicHeight / 2)
        val tempScale = radius * 2 / Math.min(srcBmp.width, srcBmp.height)
        temMatrix.postScale(tempScale, tempScale, centerX, centerY)
        canvas!!.drawBitmap(srcBmp, temMatrix, paint)

        paint.xfermode = null
        canvas!!.restoreToCount(layerId)
    }
}

inline fun ViewManager.circleImageView(theme: Int = 0, init: CircleImageView.() -> Unit): CircleImageView {
    return ankoView({ CircleImageView(it) }, theme, init)
}