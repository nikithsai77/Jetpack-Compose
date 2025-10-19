package com.android.compose

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.getTheValueOfLiveData(action : () -> Unit) : T {
    var data: T? = null
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
           data = value
           this@getTheValueOfLiveData.removeObserver(this)
        }
    }
    this.observeForever(observer)
    action()
    return data!!
}
