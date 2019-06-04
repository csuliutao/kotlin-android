package com.csu.liutao.kotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.csu.liutao.kotlin.adapters.TestAdapter
import com.csu.liutao.kotlin.layouts.MainUI
import com.csu.liutao.kotlin.layouts.RECYCLE_LAYOUT
import com.csu.liutao.kotlin.layouts.RECYCLE_VIEW
import com.csu.liutao.kviews.FloatCircleWindow
import com.csu.liutao.kviews.SwipeRecyclerLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainUI().setContentView(this)
    }
}
