package com.android.compose.mockk.part3

class UserRepositoryImpl : UserRepository {
    override fun getUserName(): Result<String> {
        return Result.success(value = "okay")
    }
}
