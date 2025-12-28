package com.android.compose.mockk.part4

interface CounterRepository {
    fun getCount(): Int
    fun increment(): Int
    fun decrement(): Int
    suspend fun reset()
    suspend fun saveCountInDb(count: Int) : Result<String>
    suspend fun loadFromNetwork(callback: suspend (Int) -> Unit)
}
