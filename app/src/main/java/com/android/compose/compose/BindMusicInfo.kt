package com.android.compose.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.compose.R
import com.android.compose.musicPlayer.MusicTrackState
import com.android.compose.musicPlayer.Track

@Composable
fun BindMusicInfo(musicTrackState: MusicTrackState, innerPadding: PaddingValues, onPrevious: () -> Unit, onNext: () -> Unit, onPlayPause: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = musicTrackState.track.image), contentDescription = null, modifier = Modifier.fillMaxWidth().height(height = 300.dp).clip(shape = RoundedCornerShape(size = 24.dp)), contentScale = ContentScale.Crop)

        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(text = musicTrackState.track.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(height = 12.dp))

        Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Text(text = musicTrackState.currentDuration.toFloat().div(other = 1000).toString())

            Slider(modifier = Modifier.weight(weight = 1f), value = musicTrackState.currentDuration.toFloat(), onValueChange = {}, valueRange = 0f..musicTrackState.maxDuration.toFloat())

            Text(text = musicTrackState.maxDuration.toFloat().div(other = 1000).toString())
        }

        Spacer(modifier = Modifier.height(height = 12.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = {
                onPrevious()
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_prev), contentDescription = null)
            }

            IconButton(onClick = {
                onPlayPause()
            }) {
                Icon(painter = painterResource(id = if (musicTrackState.isPlaying) R.drawable.ic_pause else R.drawable.ic_play), contentDescription = null)
            }

            IconButton(onClick = {
                onNext()
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_next), contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
fun MPreview() {
    BindMusicInfo(
        musicTrackState = MusicTrackState(Track(name = "First song", desc = "First song description", id = R.raw.one, image = R.drawable.one)),
        innerPadding = PaddingValues(all = 10.dp), onPrevious = {}, onNext = {}
    ) {}
}