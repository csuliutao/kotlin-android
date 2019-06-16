package com.csu.liutao.kjson

import okhttp3.OkHttpClient

class KHttpApi private constructor() {
    val clinet = OkHttpClient()

    companion object {
        val api = KHttpApi()
    }
}