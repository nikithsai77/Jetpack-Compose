package com.android.compose.mockk.part4

class CounterHelper {
    fun multiply(value: Int) = value * 10

    fun subtract(value: Int) = value - 10

    fun add(value: Int) = value.plus(other = 10)
}
