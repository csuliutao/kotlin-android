package com.csu.liutao.kotlin.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.ImageView
import org.jetbrains.anko.custom.ankoView

class CircleShapeImageView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseImageView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) return

        val srcBmp = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        drawable.draw(srcCanvas)

        val tempMatrix = Matrix()
        tempMatrix.postTranslate(centerX - srcBmp.width / 2, centerY - srcBmp.height / 2)
        val tempScale = radius * 2 / Math.min(srcBmp.width, srcBmp.height)
        tempMatrix.postScale(tempScale, tempScale, centerX, centerY)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(srcBmp, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
        paint.shader.setLocalMatrix(tempMatrix)

        canvas!!.drawCircle(centerX, centerY, radius, paint)
    }
}

inline fun ViewManager.circleShapeImageView(theme : Int = 0, init : CircleShapeImageView.() -> Unit) : CircleShapeImageView {
    return ankoView({CircleShapeImageView(it)},theme, init)
}