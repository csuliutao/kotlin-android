package com.csu.liutao.kotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.csu.liutao.kotlin.adapters.TestAdapter
import com.csu.liutao.kotlin.layouts.CHART_VIEW
import com.csu.liutao.kotlin.layouts.MainUI
import com.csu.liutao.kotlin.layouts.RECYCLE_LAYOUT
import com.csu.liutao.kotlin.layouts.RECYCLE_VIEW
import com.csu.liutao.kviews.ChartView
import com.csu.liutao.kviews.FloatCircleWindow
import com.csu.liutao.kviews.SwipeRecyclerLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainUI().setContentView(this)
        testChartView()
    }

    private fun testChartView() {
        val view = findViewById<ChartView>(CHART_VIEW)
        val xValues = mutableListOf("3/10", "3/11", "3/12", "3/13", "3/14", "3/15", "3/16", "3/17", "3/18")
        val yValues = mutableListOf(5, 4, 5, 6, 8, 2, 3, 5, 7)
        /*val xValues = mutableListOf("3/10", "3/11")
        val yValues = mutableListOf(5, 4)*/
        view.setChartData(xValues, yValues)
        view.show()
    }

}
