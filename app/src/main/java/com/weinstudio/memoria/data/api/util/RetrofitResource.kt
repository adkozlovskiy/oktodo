package com.weinstudio.memoria.data.api.util

data class RetrofitResource<out T>(
    val status: RetrofitStatus, val data: T?,
    val message: String?
) {

    companion object {

        fun <T> loading(data: T?): RetrofitResource<T> =
            RetrofitResource(status = RetrofitStatus.LOADING, data = data, message = null)

        fun <T> success(data: T): RetrofitResource<T> =
            RetrofitResource(status = RetrofitStatus.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): RetrofitResource<T> =
            RetrofitResource(status = RetrofitStatus.ERROR, data = data, message = message)

    }
}