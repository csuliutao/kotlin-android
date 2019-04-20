package com.csu.liutao.kviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

class CircleClipImageView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseImageView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) return
        canvas!!.save()
        val path = Path()
        path.addCircle(centerX, centerY, radius, Path.Direction.CW)
        canvas.clipPath(path)

        val temPaint = Paint()
        temPaint.isAntiAlias = true
        // 图片需显示部分左上角移动到裁剪区域左上角，不缩放图片
        val srcBmp = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        drawable.draw(srcCanvas)

        val tempMatrix = Matrix()
        tempMatrix.postTranslate(centerX - srcBmp.width / 2, centerY - srcBmp.height / 2)
        val tempScale = radius * 2 / Math.min(srcBmp.width, srcBmp.height)
        tempMatrix.postScale(tempScale, tempScale, centerX, centerY)

        canvas!!.drawBitmap(srcBmp, tempMatrix, temPaint)

        canvas.restore()
    }
}

inline fun ViewManager.circleClipImageView(theme : Int = 0, init : CircleClipImageView.() -> Unit) : CircleClipImageView {
    return ankoView({ CircleClipImageView(it) },theme,init)
}