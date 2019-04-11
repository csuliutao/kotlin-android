package com.csu.liutao.kotlin.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView

class CircleClipImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ImageView(context, attrs, defStyleAttr) {
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

        canvas!!.save()
        val path = Path()
        path.addCircle(centerX + paddingLeft, centerY + paddingTop, radius, Path.Direction.CW)
        canvas.clipPath(path)

        // 图片需显示部分左上角移动到裁剪区域左上角，不缩放图片
        val widthD = drawable.bounds.right - drawable.bounds.left
        val heightD = drawable.bounds.bottom - drawable.bounds.top
        canvas.save()
        canvas.translate(- widthD / 2 + radius * dm.density, - heightD / 2 + radius * dm.density)
        drawable.draw(canvas)
        canvas.restore()

        canvas.restore()
    }
}