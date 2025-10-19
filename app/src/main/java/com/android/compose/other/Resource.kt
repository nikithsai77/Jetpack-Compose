package com.android.compose.other

data class Resource<out T>(val status: Status, val data: T?, val error: String?) {
    companion object {
        fun <T> success(data: T?) : Resource<T> {
            return Resource(status = Status.SUCCESS, data = data, error = null)
        }
        fun <T> error(msg: String) : Resource<T> {
            return Resource(status = Status.ERROR, data = null, error = msg)
        }
        fun <T> loading() : Resource<T> {
            return Resource(status = Status.LOADING, data = null, error = null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
