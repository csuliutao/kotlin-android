package com.csu.liutao.kotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.csu.liutao.cotlin.R
import com.csu.liutao.kotlin.views.CircleClipImageView
import com.csu.liutao.kotlin.views.CircleImageView
import com.csu.liutao.kotlin.views.CircleShapeImageView

class MainActivity : AppCompatActivity() {
    lateinit var contentView : LinearLayout
    val widht = 500
    val height = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = LinearLayout(this)
        contentView.orientation = LinearLayout.VERTICAL
        contentView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(contentView)
        addCircleInCenter(R.mipmap.circle)
        addClipCircleInCenter(R.mipmap.circle)
        addShapCircleInCenter(R.mipmap.circle)
    }

    private fun addCircleInCenter(resourceId:Int) {
        val circleView = CircleImageView(this)
        circleView.layoutParams = ViewGroup.LayoutParams(widht,height)
        circleView.setImageResource(resourceId)
        contentView.addView(circleView)
    }

    private fun addClipCircleInCenter(resourceId:Int) {
        val circleView = CircleClipImageView(this)
        circleView.layoutParams = ViewGroup.LayoutParams(widht,height)
        circleView.setImageResource(resourceId)
        contentView.addView(circleView)
    }

    private fun addShapCircleInCenter(resourceId:Int) {
        val circleView = CircleShapeImageView(this)
        circleView.layoutParams = ViewGroup.LayoutParams(widht,height)
        circleView.setImageResource(resourceId)
        contentView.addView(circleView)
    }
}
