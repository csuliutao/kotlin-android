package com.csu.liutao.keventbus

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.Executors

class KEventBus private constructor() {
    private val eventList = ArrayList<Events>()
    private val handler = Handler(Looper.getMainLooper())
    private val thredPool = Executors.newFixedThreadPool(CORE_THREAD_NUM)

    fun publish (event : Event){
        for (tem in eventList) {
            if (tem.eventTypes.contains(event.eventType)) {
                handleMessage(tem.callback, event.message, event.eventType, tem.threadType)
            }
        }
    }

    fun regesiter (callback: Callback, vararg eventTypes: String, theadType : Int = MAIN_THREAD) {
        val list = ArrayList<String>()
        for (eventType in eventTypes) list.add(eventType)
        eventList.add(Events(callback, list, theadType))
    }

    fun unRegesiter(callback: Callback) {
        var index = eventList.size - 1
        while (index >= 0) {
            if (eventList[index].callback.equals(callback)) {
                eventList.remove(eventList[index])
                break;
            }
            index--
        }
    }

    private fun handleMessage(callback: Callback, message: Bundle?, eventType : String, theadType : Int) {
        when (theadType) {
            SUB_THREAD -> thredPool.execute(object : Runnable {
                override fun run() {
                    preMessage(callback, message, eventType)
                }
            })
            CUR_THREAD -> preMessage(callback, message, eventType)
            MAIN_THREAD -> handler.post(object : Runnable {
                override fun run() {
                    preMessage(callback, message, eventType)
                }
            })
        }
    }

    private fun preMessage(callback: Callback, message: Bundle?, eventType : String) {
        if (callback is AbsCallback) {
            if (callback.weakRef?.get() != null) {
                callback.onMessage(eventType, message)
            }
        } else {
            callback.onMessage(eventType, message)
        }
    }

   private data class Events(val callback: Callback, val eventTypes : ArrayList<String>, val threadType : Int) {
       override fun equals(other: Any?): Boolean {
           if (other !is Events) return false
           return callback.equals(other.callback)
       }
   }

    data class Event(val eventType : String, val message : Bundle?)

    interface Callback {
        fun onMessage (eventType : String, message : Bundle?)
    }

    abstract class AbsCallback(context: Context) : Callback {
        var weakRef : WeakReference<Context>? = null
        init {
            weakRef = WeakReference(context)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is AbsCallback) return false
            if (weakRef?.get() == null) return false
            return weakRef!!.get()!!.equals(other.weakRef?.get())
        }
    }

    companion object {
        val EVENT_BUS = KEventBus()
        val SUB_THREAD = 2
        val CUR_THREAD = 1
        val MAIN_THREAD = 0
        val CORE_THREAD_NUM = 1
    }
}