package com.csu.liutao.kotlin.layouts

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kviews.*
import com.csu.liutao.kviews.drawables.CircleImageLayerDrawable
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        verticalLayout {
            orientation = LinearLayout.VERTICAL

            scaleImageView {
                padding = dip(30)
            }.lparams(matchParent, wrapContent)

            tagLayout {
                background = ColorDrawable(Color.BLACK)
                textView {
                    text = "hellfgdfggggggggggggggggfgasdgsdfgasdfgd bbbbbbbbbbgo"
                    textSize = dip(14).toFloat()
                    background = ColorDrawable(Color.BLUE)
                    padding = dip(20)
                }.lparams(wrapContent, wrapContent)
                textView {
                    text = "aaaaa"
                    textSize = dip(14).toFloat()
                    padding = dip(20)
                    background = ColorDrawable(Color.RED)
                }.lparams(500, wrapContent)
                textView {
                    text = "bbbbbbbbb"
                    textSize = dip(14).toFloat()
                    background = ColorDrawable(Color.YELLOW)
                }.lparams(wrapContent, wrapContent){
                    margin = dip(20)
                }
                textView {
                    text = "ccccc"
                    textSize = dip(14).toFloat()
                    background = ColorDrawable(Color.CYAN)
                }.lparams(wrapContent, wrapContent){
                    margin = dip(20)
                }
                textView {
                    text = "dddddddddd"
                    textSize = dip(14).toFloat()
                    background = ColorDrawable(Color.GREEN)
                }.lparams(wrapContent, wrapContent){
                    margin = dip(20)
                }
                textView {
                    text = "rrr"
                    textSize = dip(14).toFloat()
                    background = ColorDrawable(Color.DKGRAY)
                }.lparams(wrapContent, wrapContent){
                    margin = dip(20)
                }
                textView {
                    text = "eee"
                    textSize = dip(14).toFloat()
                    background = ColorDrawable(Color.DKGRAY)
                }.lparams(wrapContent, wrapContent){
                    margin = dip(20)
                }
                padding = dip(10)
            }.lparams(matchParent, wrapContent)



            cameraFoldView {
                padding = dip(20)
                rotateX = 70F
                rotateAngle = 40F
                onClick {
                    val rotateHolder = PropertyValuesHolder.ofFloat("rotateAngle", 70F, 430F)
                    val rotateXHolder = PropertyValuesHolder.ofFloat("rotateX", 70F, 20F, 70F, 20F, 70F)
                    val animator = ObjectAnimator.ofPropertyValuesHolder(it, rotateHolder, rotateXHolder)
                    animator.duration = 2000
                    animator.start()
                }
            }.lparams(matchParent, dip(100))

            articleWithPicView {
                padding = dip(20)
                picRatio = 0.5F
                picPosition = PicPosition.CENTER
            }.lparams(width = matchParent, height = dip(250))

            ringProgressView {
                padding = dip(20)
                progress = 90
            }.lparams(width = dip(200), height = dip(150))


            circleImageView {
                imageResource = R.mipmap.circle
                padding = dip(10)
                circleDrawable = CircleImageLayerDrawable()
            }.lparams(width = dip(200), height = dip(150))

            dashboardView {
                startAngle = 150F
                graduateCount = 11
                padding = dip(10)
            }.lparams(width = dip(200), height = dip(150))

            pieView {
                padding = dip(10)
                pieDatas = mutableMapOf(0.3F to Color.RED, 0.2F to Color.BLUE, 0.4F to Color.GREEN, 0.1F to Color.CYAN)
                hasClickAnimator = true
                isCirclePie = true
            }.lparams(width = dip(200), height = dip(150))

        }
    }
}