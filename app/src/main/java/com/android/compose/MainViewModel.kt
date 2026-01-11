package com.android.compose

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var tag : String = ""

    fun updateTag(tag: String) {
        this.tag = tag
    }

    override fun onCleared() {
        super.onCleared()
        println("kk aa aa viewModel cleared : $tag")
    }

}
