package com.csu.liutao.kviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import kotlin.math.min

class RingProgressView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    var progress = 30
        set(value) {
            if (value < 0 || value > 100) throw Error("progress is in []0,100]")
            field = value
            text = String.format(format, value)
        }
        get() = field
    val bgColor = Color.GREEN
    val fgColor = Color.RED
    var startAngle = -30F
    var strokeWidth = dip(10)
    var textSize = dip(25)
    var textStrokeWidth = dip(1)
    var textColor = Color.BLUE
    private val format = "%d%%"
    private var text = "30%"
    private val rectF = RectF(0F,0F,0F,0F)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        rectF.set(computeCircleRectF())

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = bgColor
        canvas?.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint)

        paint.color = fgColor
        paint.strokeCap = Paint.Cap.ROUND
        canvas?.drawArc(rectF, startAngle, progress * 3.6F, false, paint)

        paint.style = Paint.Style.FILL
        paint.strokeWidth = textStrokeWidth.toFloat()
        paint.textSize = textSize.toFloat()
        paint.color = textColor
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        canvas?.drawText(text, rectF.centerX() - rect.width() / 2, rectF.centerY() + (paint.fontMetrics.descent - paint.fontMetrics.ascent) / 2, paint)
    }

}

internal fun View.computeCircleRectF () : RectF {
    val cenX = (paddingLeft.toFloat() + right - paddingRight) / 2
    val cenY = (paddingTop.toFloat() + bottom - paddingBottom) / 2
    val w = (right.toFloat() -paddingRight - paddingLeft) / 2
    val h = (bottom.toFloat() - paddingBottom - paddingTop) / 2
    val radius = min(w,h)
    val rectF = RectF(cenX - radius, cenY - radius, cenX + radius, cenY + radius)
    return rectF
}

inline fun ViewManager.ringProgressView(theme:Int = 0, init : RingProgressView.() -> Unit) :RingProgressView {
    return ankoView({RingProgressView(it)}, theme, init)
}