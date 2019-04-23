package com.csu.liutao.kviews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import kotlin.math.max
import kotlin.math.min

inline fun ViewManager.cameraFoldView(theme : Int = 0, init : CameraFoldView.() -> Unit) :CameraFoldView {
    return ankoView({CameraFoldView(it)}, theme, init)
}

class CameraFoldView(context: Context, attrs :AttributeSet? = null, defStyle : Int = 0) : View(context, attrs, defStyle) {
    var rotateX = 0F
        set(value) {
            field = value
            postInvalidate()
        }
        get() = field
    var rotateAngle = 0F
        set(value) {
            field = value
            postInvalidate()
        }
        get() = field

    var imageResource = R.mipmap.circle
        set(value) {
            field = value
            drawable = resources.getDrawable(value)
        }
    private val picRatio = 0.8F
    private val cameraX = Camera()
    private var drawable = resources.getDrawable(R.mipmap.circle)
    private var bitmap = scaleBitmap(resources.getDrawable(R.mipmap.circle))
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        cameraX.setLocation(0F, 0F, -10000 * resources.displayMetrics.density)
        paint.isDither = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = drawable.intrinsicWidth + paddingLeft + paddingRight
        val h = drawable.intrinsicHeight + paddingBottom + paddingTop
        setMeasuredDimension(resolveSize(w, widthMeasureSpec), resolveSize(h, heightMeasureSpec))
        val scaleX = (measuredWidth - paddingLeft - paddingRight) * picRatio / drawable.intrinsicWidth
        val scaleY = (measuredHeight - paddingTop - paddingBottom)* picRatio / drawable.intrinsicHeight
        bitmap = scaleBitmap(drawable, min(scaleX, scaleY))
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        val centerX = (measuredWidth + paddingLeft - paddingRight) / 2F
        val centerY = (measuredHeight - paddingBottom + paddingTop) / 2F

        canvas.save()
        canvas.translate(centerX, centerY)
        canvas.rotate(rotateAngle)
        canvas.clipRect(RectF(-centerX, -centerY, measuredWidth -centerX, 0F))
        canvas.rotate(-rotateAngle)
        canvas.drawBitmap(bitmap, - bitmap.width / 2F, - bitmap.height / 2F, paint)
        canvas.restore()


        canvas.save()
        canvas.translate(centerX, centerY)
        canvas.rotate(rotateAngle)
        canvas.clipRect(RectF(-centerX, 0F, measuredWidth -centerX, measuredHeight - centerY))
        cameraX.save()
        cameraX.rotateX(rotateX)
        cameraX.applyToCanvas(canvas)
        cameraX.restore()
        canvas.rotate(-rotateAngle)
        canvas.drawBitmap(bitmap, - bitmap.width / 2F, - bitmap.height / 2F, paint)
        canvas.restore()
    }
}

fun toBitmap (drawable : Drawable) : Bitmap {
    if (drawable is BitmapDrawable) return drawable.bitmap
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG  and Paint.FILTER_BITMAP_FLAG)
    canvas.drawFilter = paint
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return bitmap
}

fun scaleBitmap(drawable : Drawable, radio : Float = 0.5F) :Bitmap{
    val bitmap = toBitmap(drawable)
    val srcBitmap = Bitmap.createBitmap((bitmap.width * radio).toInt(), (bitmap.height * radio).toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(srcBitmap)
    val matrix = Matrix()
    matrix.postTranslate(-bitmap.width / 2F, - bitmap.height / 2F)
    matrix.postScale(radio, radio)
    matrix.postTranslate(srcBitmap.width / 2F, srcBitmap.height / 2F)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.isDither = true
    canvas.drawBitmap(bitmap, matrix, paint)
    return srcBitmap
}