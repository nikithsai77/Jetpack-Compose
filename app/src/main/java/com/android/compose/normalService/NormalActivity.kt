package com.android.compose.normalService

import android.os.Bundle
import androidx.compose.ui.*
import android.content.Intent
import com.android.compose.compose.MButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

class NormalActivity: ComponentActivity() {
    private lateinit var myJobIntentService: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myJobIntentService = Intent(this@NormalActivity, MyJobIntentService::class.java)
        setContent {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                MButton(name = "Start Service") {
                    MyJobIntentService.startTheService(applicationContext, intent = Intent())
                }

                MButton(name = "Stop Service") {
                    //We cannot stop the JobIntentService bez it is not like a normal service
                    //It has to be stop itself or killed by os, if it is killed by os then service will be retried later
                    stopService(myJobIntentService)
                }
            }
        }
    }

}