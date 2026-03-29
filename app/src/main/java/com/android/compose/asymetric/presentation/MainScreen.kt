package com.android.compose.asymetric.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.compose.asymetric.data.Keys
import com.android.compose.asymetric.presentation.util.RequestState

@Composable
fun MainScreen(
    apiKeysReady: RequestState<Boolean>,
    apiKeys: Keys?,
    onTryAgain: () -> Unit
) {
    if (apiKeysReady.isLoading()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                trackColor = if (isSystemInDarkTheme()) androidx.compose.ui.graphics.Color.White.copy(
                    alpha = 0.1f
                )
                else androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.1f)
            )
        }
    } else if (apiKeysReady.isSuccess()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            apiKeys?.let {
                Text(text = "First: ${apiKeys.firstKey}\nSecond: ${apiKeys.secondKey}")
            } ?: Text(text = "Api Keys are null.")
        }
    } else if (apiKeysReady.isError()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(all = 24.dp)
                .navigationBarsPadding()
                .animateContentSize(
                    animationSpec = tween(durationMillis = 300)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(weight = 1.2f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(size = 100.dp)
                        .alpha(alpha = 0.15f),
                    painter = painterResource(id = com.android.compose.R.drawable.ic_launcher_background),
                    contentDescription = "Error Image",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier
                        .alpha(alpha = 0.5f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    text = (apiKeysReady as RequestState.Error).parseError(),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            Box(
                modifier = Modifier.weight(weight = 1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 54.dp),
                    shape = RoundedCornerShape(size = 6.dp),
                    onClick = onTryAgain
                ) {
                    Text(text = "Try again", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
