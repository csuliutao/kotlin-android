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
        val swipeLayout = findViewById<SwipeRecyclerLayout>(RECYCLE_LAYOUT)
        val recyclerView = findViewById<RecyclerView>(RECYCLE_VIEW)
        recyclerView.adapter = TestAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeLayout.listener = object : SwipeRecyclerLayout.OnSwipeListener {
            override fun onRefresh() {
                toast("on refresh")
                swipeLayout.postDelayed({
                    swipeLayout.stopRefresh()
                }, 2000)
            }

            override fun onLoadMore() {
                toast("load more")
                swipeLayout.postDelayed({
                    swipeLayout.stopRefresh()
                }, 2000)
            }
        }


        /*window.decorView.post(Runnable {
            val floatCircle = FloatCircleWindow(this, dip(80))
            floatCircle.show()
        })*/
    }
}
