package com.csu.liutao.kotlin.layouts

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kviews.circleImageView
import com.csu.liutao.kviews.pieView
import org.jetbrains.anko.*

class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        verticalLayout {
            pieView {
                padding = dip(20)
                pieDatas = mutableMapOf(0.3F to Color.RED, 0.2F to Color.BLUE, 0.5F to Color.GREEN)
                hasClickAnimator = true
                isCirclePie = true
            }.lparams(width = dip(300), height = dip(350))
        }
    }
}