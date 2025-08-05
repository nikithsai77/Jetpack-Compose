package com.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.compose.ui.theme.ComposeTheme
import androidx.compose.animation.ExperimentalSharedTransitionApi

class MainActivity : ComponentActivity() {

    @ExperimentalSharedTransitionApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {

            }
        }
    }

}