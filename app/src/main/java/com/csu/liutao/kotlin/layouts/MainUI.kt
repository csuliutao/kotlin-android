package com.csu.liutao.kotlin.layouts

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kviews.*
import com.csu.liutao.kviews.drawables.CircleImageLayerDrawable
import org.jetbrains.anko.*

class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        verticalLayout {
            orientation = LinearLayout.VERTICAL
            articleWithPicView {
                padding = dip(20)
                picRatio = 0.5F
                picPosition = PicPosition.CENTER
            }.lparams(width = dip(400), height = dip(200))

            ringProgressView {
                padding = dip(20)
                progress = 90
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

            circleImageView {
                imageResource = R.mipmap.circle
                padding = dip(10)
                circleDrawable = CircleImageLayerDrawable()
            }.lparams(width = dip(200), height = dip(150))
        }
    }
}