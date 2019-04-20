package com.csu.liutao.kviews.drawables

import android.graphics.*

class CircleShaderDrawable: CircleDrawable() {
    override fun draw(canvas: Canvas) {
        if (drawable == null) return
        val srcBmp = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        drawable!!.draw(srcCanvas)

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