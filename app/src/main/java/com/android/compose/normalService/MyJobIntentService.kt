package com.android.compose.normalService

import android.content.Intent
import android.content.Context
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {
    private var isStarted = false
    private var mNumber: Int = -1

    companion object {
        fun startTheService(context: Context, intent: Intent) {
            enqueueWork(context, MyJobIntentService::class.java, 100, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        isStarted = true
        mGenerateNumber()
    }

    private fun mGenerateNumber() {
       for (i in 1.. 5) {
           Thread.sleep(1000)
           if (isStopped) println("kk Job Intent Service Stopped")
           mNumber++
           println("kk Job Intent Service $i th: $mNumber")
       }
    }

    override fun onStopCurrentWork(): Boolean {
        isStarted = false
        println("kk destroy")
        return super.onStopCurrentWork()
    }

}