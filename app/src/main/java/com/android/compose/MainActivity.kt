package com.android.compose

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.android.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val selectedImageUri = remember {
                        mutableStateListOf<Uri>()
                    }
                    val singlePhotoPicker = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = {
                            selectedImageUri.clear()
                            selectedImageUri.add(it!!)
                        }
                    )
                    val multiplePhotoPicker = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickMultipleVisualMedia(),
                        onResult = {
                            selectedImageUri.clear()
                            selectedImageUri.addAll(elements = it)
                        }
                    )

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            Button(onClick = {
                                singlePhotoPicker.launch(input = PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                ))
                            }) {
                                Text(text = "Pick Single Photo")
                            }

                            Button(onClick = {
                                multiplePhotoPicker.launch(input = PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                ))
                            }) {
                                Text(text = "Pick Multiple Photo's")
                            }
                        }

                        LazyVerticalGrid(columns = GridCells.Fixed(count = 2), modifier = Modifier.fillMaxSize()) {
                            items(items = selectedImageUri) {
                                AsyncImage(
                                    model = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.padding(all = 2.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}