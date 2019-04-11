package com.csu.liutao.kotlin.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat.getSystemService
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView

class CircleShapeImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ImageView(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) return

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        val centerX = (measuredWidth.toFloat() - paddingLeft - paddingRight) / 2
        var centerY = (measuredHeight.toFloat() - paddingBottom - paddingTop) / 2
        val radius = if (centerX > centerY) centerY else centerX

        val widthD = drawable.bounds.right - drawable.bounds.left
        val heightD = drawable.bounds.bottom - drawable.bounds.top
        val bitmap = Bitmap.createBitmap(widthD, heightD, Bitmap.Config.ARGB_8888)
        val bitmapC = Canvas(bitmap)
        drawable.draw(bitmapC)

        // 获取需要显示图片区域（最大正方形），未缩放
        val dstBitmap = Bitmap.createBitmap(
            bitmap, (widthD / 2 - radius * dm.density).toInt(),
            (heightD / 2 - radius * dm.density).toInt(),
            (2 * radius * dm.density).toInt(),
            (2 * radius * dm.density).toInt()
        )
        val paint = Paint()
        paint.shader = BitmapShader(dstBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)

        canvas!!.drawCircle(centerX + paddingLeft, centerY + paddingTop, radius, paint)
    }
}