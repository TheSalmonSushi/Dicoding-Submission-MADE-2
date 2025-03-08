package com.salmonboy.core.data.source.remote.network

sealed class Result<out R>{
    class Success<out T>(val data: T) : Result<T>()
    class Error(val error: String) : Result<Nothing>()
    object Loading: Result<Nothing>()
}