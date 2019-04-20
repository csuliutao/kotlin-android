package com.csu.liutao.kotlin.layouts

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kviews.circleImageView
import com.csu.liutao.kviews.drawables.CircleClipDrawable
import com.csu.liutao.kviews.drawables.CircleLayerDrawable
import com.csu.liutao.kviews.drawables.CircleShaderDrawable
import org.jetbrains.anko.*

class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            orientation = LinearLayout.VERTICAL
            circleImageView {
                imageResource = R.mipmap.circle
                padding = dip(25)
//                circleDrawable = CircleLayerDrawable()
            }.lparams(width = dip(300), height = dip(200))
        }
    }
}