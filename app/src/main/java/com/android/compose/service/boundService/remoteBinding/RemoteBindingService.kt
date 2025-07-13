package com.android.compose.service.boundService.remoteBinding

import android.app.Service
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.content.Intent
import android.widget.Toast

class RemoteBindingService : Service() {
    private var mIsRandomNumber: Int = -1
    private val mClientRequestCode : Int = 100
    private val mServiceRequestCode : Int = 200
    private var mStartTheOperation: Boolean = false

    inner class ServiceHandler: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == mClientRequestCode) {
                msg.data?.let {
                    println("kk Service Received The Request From Client: Request Code: ${msg.what}, payload: ${it.getString("request")}")
                }
                val newMessenger = Message.obtain(null, mServiceRequestCode)
                newMessenger.data.putInt("response", getNumber())
                msg.replyTo?.send(newMessenger)
            }
            super.handleMessage(msg)
        }
    }

    private val messenger = Messenger(ServiceHandler())

    override fun onBind(intent: Intent?): IBinder? {
       println("kk Remote Service: onBind")
       return if (intent!=null && intent.`package`.equals("ok")) {
           Toast.makeText(this@RemoteBindingService,"correct", Toast.LENGTH_SHORT).show()
           messenger.binder
       } else {
           Toast.makeText(this@RemoteBindingService,"not correct", Toast.LENGTH_SHORT).show()
           null
       }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("kk Remote Service onUnbind")
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("kk Remote Service Started....")
        mStartTheOperation = true
        val thread = Thread {
            randomNumberGenerator()
        }
        thread.start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun randomNumberGenerator() {
        while (mStartTheOperation) {
            try {
                Thread.sleep(1000)
                if (mStartTheOperation) {
                    mIsRandomNumber = java.util.Random().nextInt(100)
                    println("kk Remote Service mIsRandomNumber: $mIsRandomNumber")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getNumber() : Int { return  mIsRandomNumber }

    override fun onDestroy() {
        super.onDestroy()
        println("kk Remote Service destroy")
        mStartTheOperation = false
    }

}