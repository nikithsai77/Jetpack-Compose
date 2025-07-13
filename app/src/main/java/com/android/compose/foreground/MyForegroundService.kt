package com.android.compose.foreground

import android.content.Intent
import androidx.core.app.JobIntentService

class MyForegroundService: JobIntentService() {
    private var isStarted = false
    private var mNumber: Int = -1

    override fun onHandleWork(intent: Intent) {
        startForeground(1,createNotification(applicationContext))
        isStarted = true
        mGenerateNumber()
    }

    private fun mGenerateNumber() {
        while (isStarted && !isStopped) {
            Thread.sleep(1000)
            mNumber++
            println("kk MyForegroundService : $mNumber")
        }
    }

    override fun onStopCurrentWork(): Boolean {
        isStarted = false
        println("kk destroy")
        return super.onStopCurrentWork()
    }

}