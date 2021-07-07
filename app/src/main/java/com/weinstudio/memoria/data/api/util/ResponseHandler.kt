package com.weinstudio.memoria.data.api.util

import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResponseHandler {

    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> Resource.error(null, e.code().toString())
            is SocketTimeoutException -> Resource.error(null, "408")
            else -> Resource.error(null, getErrorMessage(Int.MAX_VALUE))
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            401 -> "Unauthorised"
            404 -> "Not found"
            408 -> "Timeout"
            else -> "Something went wrong"
        }
    }
}