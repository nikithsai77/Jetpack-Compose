package com.android.compose.mockk.part4

class CounterUseCase(private val counterRepository: CounterRepository) {
    fun loadCount() : Int = counterRepository.getCount()

    fun increase() : Int {
        val result = counterRepository.increment()
        return result
    }

    fun decrement() : Int {
        return counterRepository.decrement()
    }

    suspend fun resetCounter() {
        counterRepository.reset()
    }

    suspend fun saveCountInDb(count: Int) : Result<String> {
        return counterRepository.saveCountInDb(count = count)
    }

    suspend fun getCountFromNetwork(onResult: (Int) -> Unit) {
        counterRepository.loadFromNetwork { count -> onResult(count) }
    }

    fun computeWithHelper() : Int {
        val helper = CounterHelper()
        val current = loadCount()
        return helper.multiply(value = current)
    }

    fun complexComputation() : Int {
        val helper = CounterHelper()
        val current = loadCount()
        val sub = helper.subtract(value = current)
        val added = helper.add(value = sub)
        return added
    }

    suspend fun reset() {
        if (AppConfig.shouldAutoReset) counterRepository.reset()
    }

}
