package com.android.compose.jobScheduler

import android.app.job.JobService
import android.app.job.JobParameters

class MyJobService : JobService() {
    private var isStared = false
    private var mRandomNumber: Int = -1

    override fun onStartJob(params: JobParameters?): Boolean {
        println("kk JobService started")
        isStared = true
        mGenerate(params)
        //true for ling running operation, false for short operation
        return true
    }

    private fun mGenerate(params1: JobParameters?) {
        val thread = Thread {
            while (isStared) {
                Thread.sleep(1000)
                mRandomNumber++
                println("kk Random Number: $mRandomNumber")
                if (mRandomNumber == 5) isStared = false
            }
        }
        if (!isStared) this.jobFinished(params1, true)
        thread.start()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        isStared = false
        //return true if you want to restart
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        isStared = false
        println("kk destroyed")
    }

}