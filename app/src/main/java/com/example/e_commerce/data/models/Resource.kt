package com.example.e_commerce.data.models

sealed class Resource<T>(
    val data: T? = null,
    val exception : Exception? = null
) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: Exception, data: T? = null): Resource<T>(data, message)
    class Loading<T>(data: T? = null): Resource<T>(data)
}