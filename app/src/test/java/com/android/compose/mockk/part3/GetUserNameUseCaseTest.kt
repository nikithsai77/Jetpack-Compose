package com.android.compose.mockk.part3

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class GetUserNameUseCaseTest {

    @Test
    fun `return the string by using stub`() {
        // Arrange
        val useCase = GetUseCase(userRepository = UserRepositoryImpl())

        // Act
        val name = useCase()

        // Assert
        Assert.assertEquals("okay", name.getOrThrow())
    }

    @Test
    fun `return the string by using mock`() {
        // Arrange
        val userRepository = mockk<UserRepository>()
        every { userRepository.getUserName() } returns Result.success(value = "Mockk")
        val useCase = GetUseCase(userRepository = userRepository)

        // Act
        val name = useCase()

        // Assert
        Assert.assertEquals("Mockk", name.getOrThrow())
    }

    @Test
    fun `should return failure result when getUserName is called mockk`() {
        // Arrange
        val userRepository = mockk<UserRepository>()
        every { userRepository.getUserName() } returns Result.failure(exception = Exception("error"))
        val useCase = GetUseCase(userRepository = userRepository)

        // Act
        val result = useCase()

        // Assert
        Assert.assertEquals("error", result.exceptionOrNull()?.message.toString())
    }

}
