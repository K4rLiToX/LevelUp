package com.carlosdiestro.levelup.core.domain

sealed class Response<T>(val data: T? = null, val message: String? = null) {
    class Loading<T> : Response<T>(null, null)
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(message: String) : Response<T>(null, message)
}
