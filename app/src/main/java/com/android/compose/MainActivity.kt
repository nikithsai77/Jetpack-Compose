package com.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxWidth(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                )
                            }
                        )
                    }
                ) { paddingValues ->
                    val windowInfo = rememberWindowInfo()
                    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.COMPACT) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues = paddingValues)
                        ) {
                            items(count = 10) {
                                Text(
                                    text = "Item: $it",
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Cyan)
                                        .padding(all = 16.dp)
                                )
                            }

                            items(count = 10) {
                                Text(
                                    text = "Item $it",
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Yellow)
                                        .padding(all = 16.dp)
                                )
                            }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(modifier = Modifier.weight(weight = 1f)) {
                                items(count = 10) {
                                    Text(
                                        text = "Item: $it",
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Cyan)
                                            .padding(all = 16.dp)
                                    )
                                }
                            }

                            LazyColumn(modifier = Modifier.weight(weight = 1f)) {
                                items(count = 10) {
                                    Text(
                                        text = "Item: $it",
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Yellow)
                                            .padding(all = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}