package com.android.compose.musicPlayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.android.compose.compose.AddIconButton
import com.android.compose.compose.BindMusicInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MusicActivity: ComponentActivity() {
    private var isBound = false
    private var service: MusicPlayerService? = null
    private var musicStateFlow = MutableStateFlow(value = MusicTrackState())

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, bind: IBinder?) {
            isBound = true
            service = (bind as MusicPlayerService.MusicBinder).getService()
            bind.setMusicList(songs)
            lifecycleScope.launch {
                bind.getCurrentTrack().collectLatest {
                   musicStateFlow.value = it
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
           service = null
           isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize(),
                topBar = {
                    AddIconButton(imageVector = Icons.Default.PlayArrow, onclick =  {
                        val intent = Intent(this@MusicActivity, MusicPlayerService::class.java)
                        startService(intent)
                    })
                }, bottomBar = {
                    AddIconButton(imageVector = Icons.Default.Close, onclick = {
                        val intent = Intent(this@MusicActivity, MusicPlayerService::class.java)
                        stopService(intent)
                        if (isBound) unbindService(connection)
                        isBound = false
                    })
                }) { innerPadding ->

                val state by musicStateFlow.collectAsStateWithLifecycle()
                BindMusicInfo(musicTrackState = state, innerPadding = innerPadding, onNext = {
                    service?.next()
                }, onPrevious = { service?.previous() }, onPlayPause = {
                    service?.playPause()
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bindService(Intent(this@MusicActivity, MusicPlayerService::class.java), connection, BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

}