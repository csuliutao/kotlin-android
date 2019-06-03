package com.csu.liutao.kotlin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*

class TestAdapter(var context : Context) : RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    var items = arrayListOf("firstfirst","hahd", "jdjd","hello","kitto","thing","apple", "cat", "joko", "anko","hahd", "jdjd","hello","kitto","thing","apple", "cat", "joko", "anko","lastlastlast")



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val textView = TextView(context);
        textView.layoutParams = ViewGroup.LayoutParams(matchParent, context.dip(48))
        textView.gravity = Gravity.CENTER
        textView.textSize = context.sp(8).toFloat()
        return ViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.view.text = items.get(p1)
    }

    class ViewHolder(var view : TextView) : RecyclerView.ViewHolder (view){
    }
}