package com.android.compose.service.boundService.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.compose.IMyAidlInterface
import com.android.compose.Student

class AIDLService : Service() {

    private val binder = object : IMyAidlInterface.Stub() {
        override fun getData(): Student? {
            return Student(id = 1, name = "Phillip Lackner")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }


}