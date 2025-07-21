package com.android.compose.musicPlayer

data class MusicTrackState(val track: Track = Track(), val maxDuration: Int = 0, val currentDuration: Int = 0, val isPlaying : Boolean = false)