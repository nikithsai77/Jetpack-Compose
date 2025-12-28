package com.android.compose.mockk.part2

class Calculator {

    fun add(a: Int, b: Int): Int {
        return a.plus(other = b)
    }

    fun subtract(a: Int, b: Int): Int {
        return a.minus(other = b)
    }

}
