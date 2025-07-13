package com.android.compose.foreground

import android.os.Bundle
import android.content.Intent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.compose.compose.MButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.core.app.JobIntentService.enqueueWork

class ForeGroundActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                MButton(name = "Start Service") {
                    enqueueWork(this@ForeGroundActivity, MyForegroundService::class.java, 123, Intent())
                }

                MButton(name = "Stop Service") {
                }
            }
        }
    }

}