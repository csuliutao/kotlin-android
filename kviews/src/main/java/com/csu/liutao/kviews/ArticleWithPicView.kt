package com.csu.liutao.kviews

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import java.lang.Error
import kotlin.math.max
import kotlin.math.min

class ArticleWithPicView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    var text =
        "Don’t forget the things you once you owned. Treasure the things you can’t get. Don't give up the things that belong to you and keep those lost things in memory.We all live in the past. We take a minute to know someone, one hour to like someone, and one day to love someone, but the whole life to forget someone.When tomorrow turns in today, yesterday, and someday that no more important in your memory, we suddenly realize that we r pushed forward by time. This is not a train in still in which you may feel forward when another train goes by. It is the truth that we've all grown up.And we become different.Don’t forget the things you once you owned. Treasure the things you can’t get. Don't give up the things that belong to you and keep those lost things in memory.We all live in the past. We take a minute to know someone, one hour to like someone, and one day to love someone, but the whole life to forget someone.When tomorrow turns in today, yesterday, and someday that no more important in your memory, we suddenly realize that we r pushed forward by time. This is not a train in still in which you may feel forward when another train goes by. It is the truth that we've all grown up.And we become different."
    var textSize = dip(14).toFloat()
    var textColor = Color.BLUE
    var picResource = R.mipmap.circle
        set(value) {
            drawable = resources.getDrawable(picResource)
            field = value
        }
    var picPosition = PicPosition.RIGHT_CENTER
    var picRatio  = 0.5F
        set(value) {
            if (value > 0.5F || value <= 0) throw Error("picRatio is in range (0,0.5]")
            field = value
        }

    private var picWidth = 0F
    private var picHeight = 0F
    private var drawable = resources.getDrawable(R.mipmap.circle)
    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = paddingTop + paddingBottom + (drawable.bounds.height() / picRatio).toInt()
        val w = (drawable.bounds.width() / picRatio).toInt() + paddingLeft + paddingRight

        setMeasuredDimension(resolveSize(w, widthMeasureSpec), resolveSize(h, heightMeasureSpec))
        picHeight = (measuredHeight - paddingBottom - paddingTop) * picRatio
        picWidth = (measuredWidth - paddingLeft - paddingRight) * picRatio

        paint.color = textColor
        paint.textSize = textSize
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        val rectF = computeRectF()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(bitmap)
        drawable.setBounds(0, 0, bitmap.width, bitmap.height)
        drawable.draw(tempCanvas)

        canvas!!.save()
        canvas!!.clipRect(rectF)
        val matrix = Matrix()
        matrix.postTranslate(rectF.centerX() - drawable.intrinsicWidth / 2, rectF.centerY() - drawable.intrinsicHeight / 2)
        val scale = max(rectF.width() / drawable.intrinsicWidth, rectF.height() / drawable.intrinsicHeight)
        matrix.postScale(scale, scale, rectF.centerX(), rectF.centerY())
        canvas!!.drawBitmap(bitmap, matrix, null)
        canvas.restore()

        val textH = paint.fontSpacing
        var startY = paddingTop.toFloat()
        val maxH = measuredHeight.toFloat() - paddingBottom
        var tempText = text
        while (startY < maxH) {
            if (isInBounds(startY, startY + textH, rectF)) {
                val maxW = measuredWidth.toFloat() - paddingLeft - paddingRight - picWidth
                when(picPosition) {
                    PicPosition.LEFT_CENTER, PicPosition.LEFT_BOTTOM, PicPosition.LEFT_TOP -> {
                        tempText = afterDrawText(canvas, tempText, maxW, paddingLeft.toFloat() + picWidth,startY + textH)
                    }
                    PicPosition.RIGHT_TOP, PicPosition.RIGHT_CENTER,PicPosition.RIGHT_BOTTOM -> {
                        tempText = afterDrawText(canvas, tempText, maxW, paddingLeft.toFloat(),startY + textH)
                    }
                    else ->{
                        val halfMax = maxW / 2
                        tempText = afterDrawText(canvas, tempText, halfMax, paddingLeft.toFloat(),startY + textH)
                        tempText = afterDrawText(canvas, tempText, halfMax, paddingLeft.toFloat() + halfMax + picWidth,startY + textH)
                    }
                }
            } else {
                val maxW = measuredWidth.toFloat() - paddingLeft - paddingRight
                tempText = afterDrawText(canvas, tempText, maxW, paddingLeft.toFloat(),startY + textH)
            }
            startY += textH
        }

    }

    private fun afterDrawText(canvas: Canvas, tempText: String, maxW: Float, x: Float, y:Float): String {
        val count = paint.breakText(tempText, true, maxW, null)
        canvas!!.drawText(tempText.substring(0, count), x, y, paint)
        return tempText.substring(count)
    }

    private fun isInBounds(startY: Float, endY: Float, rectF: RectF): Boolean {
        when {
            startY >= rectF.top && startY <= rectF.bottom  -> return true
            endY >= rectF.top && endY <= rectF.bottom  -> return true
            endY >= rectF.bottom && startY <= rectF.top  -> return true
            else -> return false
        }
    }

    private fun computeRectF(): RectF {
        val rectF = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), picWidth + paddingLeft, picHeight + paddingTop)
        val offCX = (measuredWidth - paddingRight - picWidth  - paddingLeft) / 2
        val offCY = (measuredHeight - paddingTop - paddingBottom - picHeight) / 2
        val offX = measuredWidth - paddingRight - picWidth - paddingLeft
        val offY = measuredHeight - paddingBottom - picHeight - paddingTop
        when(picPosition) {
            PicPosition.LEFT_CENTER -> rectF.offset(0F, offCY)
            PicPosition.LEFT_BOTTOM -> rectF.offset(0F, offY)
            PicPosition.RIGHT_TOP -> rectF.offset(offX, 0F)
            PicPosition.RIGHT_CENTER -> rectF.offset(offX, offCY)
            PicPosition.RIGHT_BOTTOM -> rectF.offset(offX, offY)
            PicPosition.TOP_CENTER -> rectF.offset(offCX, 0F)
            PicPosition.BOTTOM_CENTER -> rectF.offset(offCX, offY)
            PicPosition.CENTER -> rectF.offset(offCX, offCY)
            else ->{}
        }
        return rectF
    }

}

enum class PicPosition {
    LEFT_TOP, LEFT_CENTER, LEFT_BOTTOM, RIGHT_TOP, RIGHT_CENTER, RIGHT_BOTTOM, TOP_CENTER, BOTTOM_CENTER, CENTER
}

inline fun ViewManager.articleWithPicView(theme: Int = 0, init: ArticleWithPicView.() -> Unit): ArticleWithPicView {
    return ankoView({ ArticleWithPicView(it) }, theme, init)
}