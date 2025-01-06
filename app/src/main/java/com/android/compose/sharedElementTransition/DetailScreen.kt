package com.android.compose.sharedElementTransition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@ExperimentalSharedTransitionApi
@Composable
fun SharedTransitionScope.DetailScreen(resId: Int, title: String, animatedVisibilityScope: AnimatedVisibilityScope) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(resId), contentDescription = null, modifier = Modifier.aspectRatio(ratio = 16/9f).weight(1f).sharedElement(
                state = rememberSharedContentState(key = "image/$resId"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _,_ -> tween(durationMillis = 1000) }))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = title, modifier = Modifier.weight(1f).sharedElement(
            state = rememberSharedContentState(key = "text/$title"),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _,_ ->
                tween(durationMillis = 1000)
            }
        ))
    }
}