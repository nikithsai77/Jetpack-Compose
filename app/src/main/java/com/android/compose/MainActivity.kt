package com.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.memory.MemoryCache
import coil.imageLoader

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl1 = "https://cdn.pixabay.com/photo/2016/09/07/10/37/kermit-1651325_1280.jpg"
        val imageUrl2 = "https://cdn.pixabay.com/photo/2017/01/29/14/19/kermit-2018085_1280.jpg"
        setContent {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

                SubcomposeAsyncImage(model = imageUrl1, contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(ratio = 200f / 100f),
                    loading = {
                        CircularProgressIndicator()
                    },
                    error = {
                        Text(text = "Failed to load the Image", textAlign = TextAlign.Center)
                    }
                )

                Spacer(modifier = Modifier.height(height = 20.dp))

                AsyncImage(
                    model = imageUrl2,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(ratio = 1280f / 692f)
                )

                Spacer(modifier = Modifier.height(height = 20.dp))

                Button(
                    onClick = {
                        imageLoader.memoryCache?.remove(key = MemoryCache.Key(key = imageUrl1))
                        imageLoader.diskCache?.remove(key = imageUrl1)

                        imageLoader.memoryCache?.remove(key = MemoryCache.Key(key = imageUrl2))
                        imageLoader.diskCache?.remove(key = imageUrl2)
                    }
                ) {
                    Text(text = "Clear Cache")
                }
            }
        }
    }

}