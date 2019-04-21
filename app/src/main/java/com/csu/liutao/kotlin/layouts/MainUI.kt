package com.csu.liutao.kotlin.layouts

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kviews.circleImageView
import com.csu.liutao.kviews.dashboardView
import com.csu.liutao.kviews.drawables.CircleDrawable
import com.csu.liutao.kviews.drawables.CircleImageClipDrawable
import com.csu.liutao.kviews.drawables.CircleImageLayerDrawable
import com.csu.liutao.kviews.pieView
import org.jetbrains.anko.*

class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        verticalLayout {
            orientation = LinearLayout.VERTICAL
            dashboardView {
                startAngle = 150F
                graduateCount = 11
                padding = dip(20)
            }.lparams(width = dip(300), height = dip(350))

            pieView {
                padding = dip(20)
                pieDatas = mutableMapOf(0.3F to Color.RED, 0.2F to Color.BLUE, 0.4F to Color.GREEN, 0.1F to Color.CYAN)
                hasClickAnimator = true
                isCirclePie = true
            }.lparams(width = dip(300), height = dip(350))

            circleImageView {
                imageResource = R.mipmap.circle
                padding = dip(20)
                circleDrawable = CircleImageLayerDrawable()
            }.lparams(width = dip(300), height = dip(350))
        }
    }
}