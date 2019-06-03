package com.csu.liutao.kotlin.layouts

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kviews.*
import com.csu.liutao.kviews.drawables.CircleImageLayerDrawable
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
const val RECYCLE_LAYOUT = 6;
const val RECYCLE_VIEW = 7
class MainUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View = with(ui) {
        swipeRecyclerLayout {
            id = RECYCLE_LAYOUT
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            leftPadding = dip(16)
            rightPadding = dip (16)

            recycleView {
                id = RECYCLE_VIEW
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            }
        }
    }
}