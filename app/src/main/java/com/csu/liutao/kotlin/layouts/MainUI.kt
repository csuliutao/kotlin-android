package com.csu.liutao.kotlin.layouts

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kotlin.views.circleClipImageView
import com.csu.liutao.kotlin.views.circleImageView
import com.csu.liutao.kotlin.views.circleShapeImageView
import org.jetbrains.anko.*

class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
//            gravity = Gravity.CENTER
            orientation = LinearLayout.VERTICAL
            circleImageView {
                imageResource = R.mipmap.circle
                padding = dip(15)
            }.lparams(width = dip(300), height = dip(200)) {
//                gravity = Gravity.CENTER_HORIZONTAL
            }

            circleClipImageView {
                imageResource = R.mipmap.circle
                padding = dip(10)
            }.lparams(wrapContent, wrapContent) {
//                gravity = Gravity.CENTER_HORIZONTAL
                margin = dip(20)
            }

            circleShapeImageView {
                imageResource = R.mipmap.circle
            }.lparams(width = dip(170), height = dip(300)) {
//                gravity = Gravity.CENTER_HORIZONTAL
                margin = dip(30)
            }
        }
    }
}