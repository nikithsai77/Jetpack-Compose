package com.android.compose.mockk.part3

interface UserRepository {
    fun getUserName(): Result<String>
}
