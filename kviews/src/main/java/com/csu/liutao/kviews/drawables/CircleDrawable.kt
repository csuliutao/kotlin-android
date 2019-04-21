package com.csu.liutao.kviews.drawables

import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.math.min

sealed class CircleDrawable{
    protected var radius = 0F
    protected var centerX = 0F
    protected var centerY = 0F
    var bounds : Rect? = null
        set(value) {
            field = value
            if (value != null) {
                centerX = value!!.exactCenterX()
                centerY = value!!.exactCenterY()
                radius = min(value.width(), value.height()).toFloat() / 2
            }
        }
    var drawable : Drawable? = null

    abstract fun draw(canvas: Canvas)
}

class CircleImageClipDrawable: CircleDrawable() {
    override fun draw(canvas: Canvas) {
        if (drawable == null) return
        canvas.save()
        val path = Path()
        path.addCircle(centerX, centerY, radius, Path.Direction.CW)
        canvas.clipPath(path)

        val temPaint = Paint()
        temPaint.isAntiAlias = true
        // 图片需显示部分左上角移动到裁剪区域左上角，不缩放图片
        val srcBmp = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        drawable!!.draw(srcCanvas)

        val tempMatrix = Matrix()
        tempMatrix.postTranslate(centerX - srcBmp.width / 2, centerY - srcBmp.height / 2)
        val tempScale = radius * 2 / Math.min(srcBmp.width, srcBmp.height)
        tempMatrix.postScale(tempScale, tempScale, centerX, centerY)
        canvas.drawBitmap(srcBmp, tempMatrix, temPaint)
        canvas.restore()
    }
}

class CircleImageLayerDrawable: CircleDrawable(){
    override fun draw(canvas: Canvas) {
        if (drawable == null) return
        val paint = Paint()
        paint.isAntiAlias = true
        val layerId =
            canvas!!.saveLayer(centerX - radius, centerY - radius, centerX + radius, centerY + radius, paint)
        // 绘制圆形图层
        val dstBmp = Bitmap.createBitmap((centerX + radius).toInt(), (centerY + radius).toInt(), Bitmap.Config.ARGB_8888)
        val dstCanvas = Canvas(dstBmp)
        dstCanvas.drawCircle(centerX, centerY, radius, paint)
        canvas.drawBitmap(dstBmp, 0F, 0F, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 图片需显示部分左上角移动到裁剪区域左上角，不缩放图片
        val srcBmp = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val srcCanvas = Canvas(srcBmp)
        drawable!!.draw(srcCanvas)

        val temMatrix = Matrix()
        temMatrix.postTranslate(centerX - drawable!!.intrinsicWidth / 2, centerY - drawable!!.intrinsicHeight / 2)
        val tempScale = radius * 2 / Math.min(srcBmp.width, srcBmp.height)
        temMatrix.postScale(tempScale, tempScale, centerX, centerY)
        canvas.drawBitmap(srcBmp, temMatrix, paint)

        paint.xfermode = null
        canvas.restoreToCount(layerId)
    }
}

class CircleImageShaderDrawable: CircleDrawable() {
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