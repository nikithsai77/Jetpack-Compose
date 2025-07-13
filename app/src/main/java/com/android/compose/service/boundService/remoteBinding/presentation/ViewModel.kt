package com.android.compose.service.boundService.remoteBinding.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

class ViewModel : ViewModel() {
    private var _state = mutableStateOf(value = "")
    val state = _state

    fun updateValue(result: String) {
        _state.value = result
    }

}