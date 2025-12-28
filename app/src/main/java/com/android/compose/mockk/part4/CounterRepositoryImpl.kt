package com.android.compose.mockk.part4

import kotlinx.coroutines.delay

class CounterRepositoryImpl : CounterRepository {
    private var count: Int = 0

    override fun getCount(): Int {
        return count
    }

    override fun increment(): Int {
       return count++
    }

    override fun decrement(): Int {
        return count--
    }

    override suspend fun reset() {
        count = 0
    }

    override suspend fun saveCountInDb(count: Int): Result<String> {
      this.count = count
      return Result.success(value = "Success")
    }

    override suspend fun loadFromNetwork(callback: suspend (Int) -> Unit) {
        delay(timeMillis = 1000L)
        callback(NETWORK_RESPONSE)
    }

    companion object {
        const val NETWORK_RESPONSE = 200
    }

}
