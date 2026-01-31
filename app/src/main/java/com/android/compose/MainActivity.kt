package com.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.compose.ui.theme.CustomLayoutTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomLayoutTheme {
                CustomFlowRow {
                    repeat(times = 10) {
                        Box(
                            modifier = Modifier
                                .width(width = Random.nextInt(from = 50, until = 200).dp)
                                .height(height = 100.dp)
                                .background(
                                    Color(
                                        color = Random.nextLong(until = 0xFF000000)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }

}