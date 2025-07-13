package com.android.compose.service.boundService.remoteBinding

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.compose.service.boundService.remoteBinding.presentation.ViewModel
import com.android.compose.ui.theme.ComposeTheme

class SecondActivity: ComponentActivity() {
    private var isBound = false
    private lateinit var serviceIntent: Intent
    private val mClientRequestCode : Int = 100
    private val mServiceRequestCode : Int = 200
    private var serviceMessenger: Messenger? = null
    private var receiveMessenger: Messenger? = null
    private val viewModels: ViewModel by viewModels()

    private class ActivityHandler(private val mServiceRequestCode: Int, private val onReply: (String) -> Unit): Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            println("kk got Server Result: ${msg.what} ,,, ${msg.data.getInt("response")}")
            if (msg.what == mServiceRequestCode) onReply("Random Number : ${msg.data.getInt("response")}")
            super.handleMessage(msg)
        }
    }

    private val connection : ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            isBound = true
            serviceMessenger = Messenger(binder)
            receiveMessenger = Messenger(ActivityHandler(mServiceRequestCode) { result ->
                viewModels.updateValue(result)
            })
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            serviceMessenger = null
            receiveMessenger = null
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                serviceIntent = Intent()
                serviceIntent.setComponent(
                    ComponentName(
                        "com.android.compose",
                        "com.android.compose.service.boundService.remoteBinding.RemoteBindingService"
                    )
                )
                //to restrict the client app to connect the service.
//                serviceIntent.setPackage("ok")
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = "Remote Binding Service" , color = Color.Black) })
                }) { innerPadding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = viewModels.state.value)

                            Spacer(modifier = Modifier.height(height = 10.dp))

                            Button(onClick = {
                                startService(serviceIntent)
                            }) {
                                Text(text = "Start Service")
                            }

                            Spacer(modifier = Modifier.height(height = 10.dp))

                            Button(onClick = {
                                stopService(serviceIntent)
                            }) {
                                Text(text = "Stop Service")
                            }

                            Spacer(modifier = Modifier.height(height = 10.dp))

                            Button(onClick = {
                                bindService(serviceIntent, connection, BIND_AUTO_CREATE)
                            }) {
                                Text(text = "Bind Service")
                            }

                            Spacer(modifier = Modifier.height(height = 10.dp))

                            Button(onClick = {
                                if (isBound) {
                                    unbindService(connection)
                                    isBound = false
                                }
                            }) {
                                Text(text = "UnBind Service")
                            }

                            Spacer(modifier = Modifier.height(height = 10.dp))

                            Button(onClick = {
                                if (isBound) {
                                    val bundle = Bundle()
                                    bundle.putString("request", "Requesting The Random Number")

                                    val newMessage = Message.obtain(null, mClientRequestCode)
                                    newMessage.data = bundle
                                    newMessage.replyTo = receiveMessenger

                                    serviceMessenger?.send(newMessage)

                                } else viewModels.updateValue(result = "Service Not Bounded")
                            }) {
                                Text(text = "Get The Random Number")
                            }
                        }
                    }
                }
            }
        }
    }

}