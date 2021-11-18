package com.example.localbuddy.data

import okhttp3.ResponseBody

sealed class Resource<out T> {
    data class Success<out T>(val value: T): Resource<T>()

    data class Faliure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ): Resource<Nothing>()
}