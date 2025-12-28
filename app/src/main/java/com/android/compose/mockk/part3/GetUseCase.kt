package com.android.compose.mockk.part3

class GetUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.getUserName()
}
