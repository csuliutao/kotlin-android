package com.csu.liutao.kotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.csu.liutao.kotlin.layouts.MainUI
import com.csu.liutao.kviews.FloatCircleWindow
import org.jetbrains.anko.dip
import org.jetbrains.anko.setContentView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainUI().setContentView(this)
        window.decorView.post(Runnable {
            val floatCircle = FloatCircleWindow(this, dip(80))
            floatCircle.show()
        })
    }
}
