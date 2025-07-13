package com.android.compose.service.boundService.localBinding

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.compose.service.boundService.remoteBinding.presentation.ViewModel

class LocalBindingActivity: ComponentActivity() {
    private var isBound = false
    private lateinit var mLocalService: Intent
    private val viewModels: ViewModel by viewModels()
    private var connection: ServiceConnection? = null
    private lateinit var myService: RandomNumberService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mLocalService = Intent(applicationContext, RandomNumberService::class.java)
            Column(
                modifier = Modifier.Companion.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {

                Text(text = viewModels.state.value)

                Spacer(modifier = Modifier.Companion.height(height = 5.dp))

                Button(onClick = { startService(mLocalService) }) { Text(text = "Start Service") }

                Spacer(modifier = Modifier.Companion.height(height = 5.dp))

                Button(onClick = { stopService(mLocalService) }) { Text(text = "Stop Service") }

                Spacer(modifier = Modifier.Companion.height(height = 5.dp))

                Button(onClick = {
                    bindService {
                        viewModels.updateValue(result = it)
                    }
                }) { Text(text = "Bind Service") }

                Spacer(modifier = Modifier.Companion.height(height = 5.dp))

                Button(onClick = { unBind() }) { Text(text = "UnBind Service") }

                Spacer(modifier = Modifier.Companion.height(height = 5.dp))

                Button(onClick = { viewModels.updateValue(result = if (isBound) "Random Number is ${myService.getRandomNumber()}" else "Service Not Bound") }) {
                    Text(
                        text = "Get Random Number"
                    )
                }
            }
        }
    }

    private fun bindService(onclick: (String) -> Unit) {
        if (connection==null) {
            connection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                    onclick("Service is Bounded")
                    isBound = true
                    binder?.let {
                        myService = (it as RandomNumberService.ServiceBinder).getService()
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    isBound = false
                }
            }
        }
        bindService(mLocalService, connection!!, BIND_AUTO_CREATE)
    }

    private fun unBind() {
        if (isBound && connection != null) {
            unbindService(connection!!)
            isBound = false
        }
    }

}