package com.android.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ListScreen(navigate: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        repeat(times = 50) {
            item {
                Text(text = "Item ${it + 1}", modifier = Modifier.clickable {
                    navigate("Item ${it + 1}")
                })

                HorizontalDivider(Modifier, DividerDefaults.Thickness, color = DividerDefaults.color)
            }
        }
    }
}
