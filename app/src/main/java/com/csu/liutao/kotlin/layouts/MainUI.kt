package com.csu.liutao.kotlin.layouts

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kviews.circleImageView
import com.csu.liutao.kviews.ringProgressView
import com.csu.liutao.kviews.dashboardView
import com.csu.liutao.kviews.drawables.CircleImageLayerDrawable
import com.csu.liutao.kviews.pieView
import org.jetbrains.anko.*

class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        verticalLayout {
            orientation = LinearLayout.VERTICAL
            ringProgressView {
                padding = dip(20)
                progress = 90
            }.lparams(width = dip(200), height = dip(150))

            dashboardView {
                startAngle = 150F
                graduateCount = 11
                padding = dip(10)
                background = ColorDrawable(Color.BLACK)
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