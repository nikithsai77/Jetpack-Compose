package com.android.compose.service.boundService.localBinding

import android.os.Binder
import android.os.IBinder
import android.app.Service
import android.content.Intent
import java.util.Random

class RandomNumberService : Service() {
    private var isServiceStarted = false
    private val binder = ServiceBinder()
    private var mRandomNumber : Int = -1

    init {
        println("kk service init")
    }

    override fun onCreate() {
        super.onCreate()
        println("kk service onCreate")
    }

    inner class ServiceBinder : Binder() {
        fun getService() : RandomNumberService {
            return this@RandomNumberService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        println("kk service Bind")
        return binder
    }

    override fun onRebind(intent: Intent?) {
        println("kk service onRebind")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("kk service onUnBind")
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("kk service started")
        isServiceStarted = true
        val runnable = Runnable { mStartNumberGenerator() }
        val thread = Thread(runnable)
        thread.start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun mStartNumberGenerator() {
        while (isServiceStarted) {
            Thread.sleep(1000)
            mRandomNumber = Random().nextInt(100) + 0
            println("kk service mRandomNumber: $mRandomNumber")
        }
    }

    fun getRandomNumber() : Int { return mRandomNumber }

    override fun onDestroy() {
        super.onDestroy()
        println("kk service destroy")
        isServiceStarted = false
    }

}