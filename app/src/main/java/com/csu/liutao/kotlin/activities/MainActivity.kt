package com.csu.liutao.kotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.csu.liutao.kotlin.layouts.MainUI
import org.jetbrains.anko.setContentView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainUI().setContentView(this)
    }
}
