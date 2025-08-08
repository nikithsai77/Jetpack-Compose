package com.android.compose.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

suspend fun <T> LiveData<T>.getTheValue(action: suspend () -> Unit) : T? {
    var data: T? = null
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
        }
    }
    this.observeForever(observer)
    action()
    this.removeObserver(observer)
    return data
}