package com.android.compose.musicPlayer

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.android.compose.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

const val PREV = "Prev"
const val NEXT = "Next"
const val PLAY_PAUSE = "PlayPause"

class MusicPlayerService : Service() {

    private var job: Job? = null
    private var musicList = mutableListOf<Track>()
    private val scope = CoroutineScope(context = Dispatchers.Default)
    private val currentTrack = MutableStateFlow(value = MusicTrackState())
    private var mediaPlayer = MediaPlayer()
    private var localBinder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService() : MusicPlayerService = this@MusicPlayerService

        fun getCurrentTrack() = this@MusicPlayerService.currentTrack

        fun setMusicList(list: List<Track>) {
            this@MusicPlayerService.musicList = list.toMutableList()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return localBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                PREV -> {
                    previous()
                }
                NEXT -> {
                    next()
                }
                PLAY_PAUSE -> {
                    playPause()
                }
                else -> {
                    currentTrack.update {  track ->
                        track.copy(track = songs[0])
                    }
                    play(track = currentTrack.value.track)
                }
            }
        }
        return START_REDELIVER_INTENT
    }

    fun previous() {
        job?.cancel()
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer()

        val currentIndex = musicList.indexOf(currentTrack.value.track)
        val previousIndex = if (currentIndex <= 0) musicList.size.minus(other = 1) else currentIndex.minus(other = 1)
        val previousItem = musicList[previousIndex]

        currentTrack.update {
            it.copy(
                track = previousItem
            )
        }

        mediaPlayer.setDataSource(this, getRawUri(id = currentTrack.value.track.id))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            sendNotification(track = currentTrack.value.track)
            updateDuration()
        }
    }

    fun next() {
        job?.cancel()
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer()

        var index = musicList.indexOf(currentTrack.value.track)
        index++
        val nextIndex = if (index <= musicList.size - 1) index else 0
        val nextItem = musicList[nextIndex]
        currentTrack.update {
            it.copy(track = nextItem)
        }

        mediaPlayer.setDataSource(this, getRawUri(id = currentTrack.value.track.id))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            sendNotification(track = currentTrack.value.track)
            updateDuration()
        }
    }

    fun playPause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
        else mediaPlayer.start()
        sendNotification(track = currentTrack.value.track)
    }

    fun play(track: Track) {
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, getRawUri(id = track.id))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            currentTrack.update {
                it.copy(
                    track = it.track,
                    isPlaying = it.isPlaying,
                    maxDuration = mediaPlayer.duration
                )
            }
            sendNotification(track = track)
            updateDuration()
        }
    }

    private fun getRawUri(id: Int) = "android.resource://${packageName}/${id}".toUri()

    fun sendNotification(track: Track) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(track.name)
            .setContentText(track.desc)
            .addAction(R.drawable.ic_prev, "Previous", createAction(id = 0, str = PREV))
            .addAction(
                if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                if (mediaPlayer.isPlaying) "Pause" else "Play",
                createAction(id = 1, str = PLAY_PAUSE)
            )
            .addAction(R.drawable.ic_next, "Next", createAction(id = 2, str = NEXT))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) startForeground(1, notification)
        else startForeground(1, notification)
    }

    fun createAction(id: Int, str: String): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            action = str
        }
        return PendingIntent.getService(this, id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun updateDuration() {
        job = scope.launch {
            if (mediaPlayer.isPlaying.not()) return@launch
            while (isActive) {
                currentTrack.update {
                    it.copy(track = it.track,
                        isPlaying = it.isPlaying,
                        maxDuration = it.maxDuration,
                        currentDuration = mediaPlayer.currentPosition
                    )
                }
                delay(timeMillis = 1000L)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

}