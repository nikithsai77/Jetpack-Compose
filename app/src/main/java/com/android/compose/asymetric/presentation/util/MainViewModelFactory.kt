package com.android.compose.asymetric.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.compose.asymetric.domain.EncryptedPreferences
import com.android.compose.asymetric.presentation.MainViewModel

class MainViewModelFactory(
    private val preferences: EncryptedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
