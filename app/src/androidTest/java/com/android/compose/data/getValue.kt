package com.android.compose.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.getValue(action : () -> Unit) : T? {
    var data: T? = null
    val observe = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
        }
    }
    this.observeForever(observe)
    Thread.sleep(2000)
    this.removeObserver(observe)
    return data
}