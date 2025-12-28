package com.android.compose.mockk.part4

import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CounterUseCaseTest {

    @Test
    fun `when Increment Called it should Trigger The Increment Of The Repository`() {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        every { repository.increment() } returns 10

        // Act
        val result = useCase.increase()

        // Assert
        verify(exactly = 1) { repository.increment() }
        Assert.assertEquals(10, result)
    }

    @Test
    fun `when getCount is Called It Should Trigger The GetCount Of The Repository`() {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        every { repository.getCount() } returns 100

        // Act
        val result = useCase.loadCount()

        // Assert
        Assert.assertEquals(100, result)
        verify(exactly = 1) { repository.getCount() }
    }

    @Test
    fun `when Decrement Called it should Trigger The Decrement Of The Repository`() {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        every { repository.decrement() } returns 9

        // Act
        val result = useCase.decrement()

        // Assert
        Assert.assertEquals(9, result)
    }

    @Test
    fun `when The Reset is Called It Should Trigger The Reset Of Repository`() = runTest {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        coEvery { repository.reset() } returns Unit

        // Act
        useCase.resetCounter()

        // Assert
        coVerify(exactly = 1) { repository.reset() }
    }

    @Test
    fun `when The SaveCountInDb is Called Then It Should Be Trigger The saveCountInDb of Repository`() = runTest {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        coEvery { repository.saveCountInDb(count = any()) } returns Result.success(value = "Success")

        // Action
        val result = useCase.saveCountInDb(count = 10)

        // Assertion
        Assert.assertEquals("Success", result.getOrDefault(defaultValue = "Failed"))
    }

    @Test
    fun `when Increment And Decrement Called It Should Trigger The Increment And Decrement Of Repository And Verify Order`() {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)

        every { repository.increment() } returns 1
        every { repository.decrement() } returns 0
        every { repository.getCount() } returns 0

        // Act
        useCase.increase()
        useCase.decrement()

        // Assert
        verifyOrder {
            repository.increment()
            repository.decrement()
        }
    }

    @Test
    fun `when Increment And Decrement Called It Should Trigger The Increment And Decrement Of Repository And Verify Sequences`() {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)

        every { repository.increment() } returns 1
        every { repository.decrement() } returns 0
        every { repository.getCount() } returns 0

        // Act
        useCase.increase()
        useCase.decrement()

        // Assert
        verifySequence {
            repository.increment()
            repository.decrement()
        }
    }

    @Test
    fun `when Condition Reset Is Called It Should Trigger Reset Of Repository`() = runTest {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        val appConfig = mockkObject(AppConfig)
        println("kk aa $appConfig")

        every { AppConfig.shouldAutoReset } returns true
        coEvery { repository.reset() } returns Unit

        // Act
        useCase.reset()

        // Assert
        coVerify(exactly = 1) { repository.reset() }
    }

    @Test
    fun `when ComputeWithHelper Called Should Return Correct Value`() {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        mockkConstructor(CounterHelper::class)

        every { repository.getCount() } returns 10
        every { anyConstructed<CounterHelper>().multiply(value = any()) } returns 200

        // Act
        val result = useCase.computeWithHelper()

        // Assertion
        Assert.assertEquals(200, result)

        unmockkConstructor(CounterHelper::class)
        clearMocks(repository)
    }

    @Test
    fun `when Get CountFromNetwork It Should Return Correct Value`() = runTest {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)

        coEvery { repository.loadFromNetwork(callback = any()) } coAnswers {
            val callBack = arg<suspend (Int) -> Unit>(n = 0)
            callBack(200)
        }

        var networkCount = -1

        // Action
        useCase.getCountFromNetwork {
            networkCount = it
        }

        // Assertion
        Assert.assertEquals(200, networkCount)
    }

    @Test
    fun `when Complex Computation Is Called Then It Should Return Correct Value`() = runTest {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        mockkConstructor(CounterHelper::class)
        every { repository.getCount() } returns 100
        every { anyConstructed<CounterHelper>().subtract(value = any()) } returns 90
        every { anyConstructed<CounterHelper>().add(any()) } returns 100

        // Act
        val result = useCase.complexComputation()

        // Assert
        Assert.assertEquals(100, result)
        unmockkConstructor(CounterHelper::class)
        clearMocks(repository)
    }

    @Test
    fun `when ComplexComputation Is Called Then It Should Return The CorrectValue`() {
        // Arrange
        val repository = mockk<CounterRepository>()
        val useCase = CounterUseCase(counterRepository = repository)
        val spy = spyk<CounterHelper>()
        mockkConstructor(CounterHelper::class)
        every { repository.getCount() } returns 100
        every { anyConstructed<CounterHelper>().subtract(value = any()) } answers {
            spy.subtract(value = firstArg())
        }
        every { anyConstructed<CounterHelper>().add(value = any()) } answers {
            spy.add(value = firstArg())
        }

        // Act
        val result = useCase.complexComputation()

        // Assertion
        Assert.assertEquals(100, result)
    }

}
