package com.android.compose.asymetric.presentation.util

sealed interface RequestState<out T> {
    data object Idle : RequestState<Nothing>
    data object Loading : RequestState<Nothing>
    data class Success<T>(val data: T) : RequestState<T>
    data class Error(val message: String) : RequestState<Nothing> {
        fun parseError(): String {
            return if (message.contains(other = "failed to connect to")) "Failed to connect to the server."
            else if (message.contains(other = "timeout")) "Connection Timeout. Please Try Again Later!"
            else message
        }
    }

    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error

}
