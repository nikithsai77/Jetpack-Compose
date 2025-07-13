package com.android.compose.service.boundService.both

import android.content.Intent
import android.app.IntentService
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import java.util.Random

class BoundService : IntentService(BoundService::class.java.name) {
    private var mRandomNumber : Int = -1
    private var mClientRequest = 100
    private var mServiceRequest = 200
    private var isBoundServiceStarted = false

    inner class LocalBinding: Binder() {
        fun getService() : BoundService {
            return this@BoundService
        }
    }

    private val localBinding = LocalBinding()

    class ServiceHandler(private val boundService: BoundService) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == boundService.mClientRequest) {
                msg.data?.let {
                    val message = Message.obtain(null, boundService.mServiceRequest)
                    message.data.putInt("result", boundService.getRandomNumber())
                    msg.replyTo.send(message)
                }
            }
            super.handleMessage(msg)
        }
    }

    val messenger: Messenger = Messenger(ServiceHandler(boundService = this@BoundService))

    override fun onBind(intent: Intent?): IBinder? {
        println("kk onBind")
        return intent?.let {
            if (it.getBooleanExtra("isRemote", false)) messenger.binder else localBinding
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("kk onUnbind")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onHandleIntent(intent: Intent?) {
        println("kk started")
        isBoundServiceStarted = true
        generateRandomNumber()
    }

    private fun generateRandomNumber() {
        while (isBoundServiceStarted) {
            Thread.sleep(1000)
            mRandomNumber = Random().nextInt(100)
            println("kk BoundService: $mRandomNumber")
        }
    }

    fun getRandomNumber() : Int {
        return mRandomNumber
    }

    override fun onDestroy() {
        super.onDestroy()
        println("kk destroyed")
        mRandomNumber = 0
        isBoundServiceStarted = false
    }

}