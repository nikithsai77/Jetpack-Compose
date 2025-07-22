package com.android.compose.locationTracking

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.compose.compose.MButton

@Composable
fun LocationGranted(context: Context) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        MButton(name = "Start Service") {
            val intent = Intent(context, LocationService::class.java)
            context.startService(intent)
        }

        MButton(name = "Stop Service") {
            val intent = Intent(context, LocationService::class.java)
            context.stopService(intent)
        }
    }
}