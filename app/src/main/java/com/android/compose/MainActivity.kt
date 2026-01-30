package com.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.compose.ui.theme.LazyStaggeredGridComposeTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = (1..100).map {
            ListItem(
                height = Random.nextInt(from = 100, until = 300).dp,
                color = Color(
                    color = Random.nextLong(until = 0xFFFFFFFF)
                ).copy(alpha = 1f)
            )
        }
        enableEdgeToEdge()
        setContent {
            LazyStaggeredGridComposeTheme {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(minSize = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                    verticalItemSpacing = 16.dp,
                    contentPadding = PaddingValues(all = 16.dp)
                ) {
                    items(items) {
                        RandomColorBox(item = it)
                    }
                }
            }
        }
    }

}

data class ListItem(val height: Dp, val color: Color)

@Composable
fun RandomColorBox(item: ListItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = item.height)
            .background(color = item.color)
            .clip(shape = RoundedCornerShape(size = 10.dp))
    )
}
