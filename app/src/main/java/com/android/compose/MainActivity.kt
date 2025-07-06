package com.android.compose

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.LocaleList
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.compose.service.boundService.TranslatorBoundService
import com.android.compose.service.boundService.TranslatorBoundService.CustomIBinder

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private var isBound = false
    private val speechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(this)
    }
    private var boundService: TranslatorBoundService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            isBound = true
            boundService = (binder as CustomIBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
           isBound = false
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.safeContentPadding().fillMaxSize()) {
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {  isGranted ->
                        if (!isGranted) Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                )
                var listen by remember {
                    mutableStateOf(value = false)
                }
                var userEnteredText by remember {
                    mutableStateOf(value = "")
                }
                LaunchedEffect(key1 = Unit) {
                    permissionLauncher.launch(input = Manifest.permission.RECORD_AUDIO)
                }
                LaunchedEffect(key1 = listen) {
                    if (listen) {
                        listen(
                            onStart = {
                                userEnteredText = "start listening"
                            }, onResult = {
                                userEnteredText = it
                                boundService?.translate(text = userEnteredText, onResult = { resultText ->
                                    userEnteredText = resultText
                                    listen = false
                                }, onFailure = {
                                    listen = false
                                })
                            }
                        )
                    }
                }
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = "Translator")})
                }, floatingActionButton = {
                        Card(colors = CardDefaults.cardColors(
                            containerColor = if (listen) Color.Green else Color.Red)
                        ) {
                            IconButton(onClick = {
                                listen = !listen
                            }) {
                                Icon(imageVector = Icons.Default.Star, contentDescription = null)
                            }
                        }
                    }
                ) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        OutlinedTextField(value = userEnteredText, onValueChange = {
                            userEnteredText = it
                        }, modifier = Modifier.fillMaxWidth(), minLines = 4)

                        Spacer(modifier = Modifier.height(height = 10.dp))

                        androidx.compose.material3.Button(onClick = {
                            boundService?.translate(
                                text = userEnteredText,
                                onResult = {
                                    userEnteredText = it
                                },
                                onFailure = {
                                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }) {
                            Text(text = "Translate")
                        }
                    }
                }
            }
        }
    }

    private fun listen(onStart: () -> Unit, onResult: (String) -> Unit) {
        val intent = Intent().apply {
            action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, LocaleList.getDefault())
        }
        speechRecognizer.setRecognitionListener(object  : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { }

            override fun onBeginningOfSpeech() {
                onStart()
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                speechRecognizer.stopListening()
            }

            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                results?.let {
                    val result = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.first()
                    result?.let(block = onResult)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        speechRecognizer.startListening(intent)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this@MainActivity, TranslatorBoundService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

}