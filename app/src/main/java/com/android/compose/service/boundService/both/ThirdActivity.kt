package com.android.compose.service.boundService.both

import android.os.*
import android.content.Intent
import android.content.ComponentName
import android.content.ServiceConnection
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.compose.service.boundService.remoteBinding.presentation.ViewModel
import com.android.compose.ui.theme.ComposeTheme

class ThirdActivity : ComponentActivity() {
    private var isBound = false
    private var isRemoteBound = false
    private lateinit var intentService: Intent
    private val viewModel: ViewModel by viewModels()
    private var mClientRequest = 100
    private var mServiceRequest = 200
    private var isRemote = false
    private var serviceMessenger: Messenger? = null
    private var componentMessenger: Messenger? = null
    private var connection: ServiceConnection? = null
    private var remoteConnection: ServiceConnection? = null
    private lateinit var localBindinService: BoundService

    class ComponentHandler(private val mServiceRequest: Int, private val onResult: (String) -> Unit): Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == mServiceRequest) {
                msg.data?.let {
                    println("kk client side: $it")
                    val result = "Random Number is ${it.getInt("result",-2)}"
                    onResult(result)
                }
            }
            super.handleMessage(msg)
        }
    }

    private fun setUp() {
        intentService = Intent(this@ThirdActivity, BoundService::class.java)
        intentService.putExtra("isRemote", isRemote)

        connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                println("kk Local Binding Service")
                if (binder!=null && binder is BoundService.LocalBinding) {
                    localBindinService = binder.getService()
                    isBound = true
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isBound = false
            }
        }

        remoteConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                println("kk Remote Binding Service")
                isRemoteBound = true
                serviceMessenger = Messenger(binder)
                componentMessenger = Messenger(ComponentHandler(mServiceRequest = mServiceRequest, onResult = viewModel::updateValue))
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isRemoteBound = false
                serviceMessenger = null
                componentMessenger = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setUp()
            ComposeTheme {
                Scaffold(topBar = {
                    Text(text = "Local and Remote Binding Service", modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.primary).padding(all = 16.dp), fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
                })
                { innerPadding ->
                    Column(modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(text = viewModel.state.value)

                        Spacer(modifier = Modifier.height(height = 10.dp))

                        MButton(name = "Start Service") {
                            startService(intentService)
                        }

                        MButton(name = "Stop Service") {
                            stopService(intentService)
                        }

                        MButton(name = "Bind Service") {
                            bindService(intentService, if (isRemote) remoteConnection!! else connection!!, BIND_AUTO_CREATE)
                        }

                        MButton(name = "UnBind Service") {
                            if (isBound || isRemoteBound) {
                                unbindService(if (isRemote) remoteConnection!! else connection!!)
                                isBound = false
                                isRemoteBound = false
                            }
                        }

                        MButton(name = "Get Random Number") {
                            val result = if (isBound) "Random Number is ${localBindinService.getRandomNumber()}"
                            else if (isRemoteBound) {

                                val requestMessenger = Message.obtain(null, mClientRequest)
                                requestMessenger.data.putString("Request", "Give Me The Random Number")
                                requestMessenger.replyTo = componentMessenger

                                serviceMessenger?.send(requestMessenger)
                                return@MButton
                            }
                            else "Service is Not Bounded"
                            viewModel.updateValue(result = result)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MButton(name: String, click: () -> Unit) {
        Button(onClick = { click() }) {
            Text(text = name)
        }
        Spacer(modifier = Modifier.height(height = 10.dp))
    }

}