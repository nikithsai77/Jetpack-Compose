package com.android.compose.musicPlayer

import android.os.Build
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

const val CHANNEL_ID = "channel_id"
const val CHANNEL_NAME = "channel_name"
const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}