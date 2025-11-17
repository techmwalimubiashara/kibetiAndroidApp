package com.mb.kibeti.forgot_password.utils

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()
}