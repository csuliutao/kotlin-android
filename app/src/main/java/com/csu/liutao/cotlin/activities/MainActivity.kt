package com.csu.liutao.cotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.csu.liutao.cotlin.R
import com.csu.liutao.cotlin.views.CircleImageView

class MainActivity : AppCompatActivity() {
    lateinit var contentView : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = FrameLayout(this)
        contentView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(contentView)
        addCircleInCenter(R.mipmap.circle)
    }

    private fun addCircleInCenter(resourceId:Int) {
        val circleView = CircleImageView(this)
        circleView.scaleType = ImageView.ScaleType.CENTER
        circleView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        circleView.setImageResource(resourceId)
        contentView.addView(circleView)
    }
}
