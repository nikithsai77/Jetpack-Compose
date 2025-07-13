package com.android.compose.service.boundService.aidl

import android.os.Bundle
import android.os.IBinder
import android.content.Intent
import androidx.activity.viewModels
import android.content.ServiceConnection
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.content.ComponentName
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.compose.IMyAidlInterface
import com.android.compose.service.boundService.remoteBinding.presentation.ViewModel

class AIDLActivity : ComponentActivity() {
    private var aidl: IMyAidlInterface? = null
    private val viewModel : ViewModel by viewModels()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            binder?.let {
                aidl = IMyAidlInterface.Stub.asInterface(it)
                viewModel.updateValue(result = aidl!!.data.toString())
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            aidl = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent()
        intent.component = ComponentName("com.android.compose","com.android.compose.service.boundService.aidl.AIDLService")
        bindService(intent, connection, BIND_AUTO_CREATE)
        setContent {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = viewModel.state.value)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

}