package com.android.compose.foreground

import android.os.Build
import com.android.compose.R
import android.content.Context
import android.app.Notification
import android.app.NotificationManager
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat

fun createNotification(context: Context): Notification {
    val channelId = "foreground_channel_id"
    val channelName = "Foreground Service Channel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    return NotificationCompat.Builder(context, channelId)
        .setContentTitle("Running Foreground Service")
        .setContentText("Your service is doing important work...")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .build()
}