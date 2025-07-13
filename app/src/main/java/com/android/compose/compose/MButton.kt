package com.android.compose.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MButton(name: String, click: () -> Unit) {
    Button(onClick = { click() }) {
        Text(text = name)
    }
    Spacer(modifier = Modifier.height(height = 10.dp))
}