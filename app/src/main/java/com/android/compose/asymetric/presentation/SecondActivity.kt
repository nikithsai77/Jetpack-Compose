package com.android.compose.asymetric.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.android.compose.asymetric.data.EncryptedPreferencesImpl
import com.android.compose.asymetric.presentation.util.MainViewModelFactory
import com.android.compose.ui.theme.ComposeTheme

class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                val viewModel: MainViewModel by viewModels {
                    MainViewModelFactory(preferences = EncryptedPreferencesImpl(context = this))
                }
                val apiKeysReady by viewModel.apiKeysReady
                val apiKeys by viewModel.apiKeys
                MainScreen(
                    apiKeysReady = apiKeysReady,
                    apiKeys = apiKeys
                ) {
                    viewModel.fetchData()
                }
            }
        }
    }

}
