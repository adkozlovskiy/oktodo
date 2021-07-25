package com.weinstudio.oktodo.data.model

/**
 * A sealed class to store the state of going online.
 */
sealed class Hike {

    object Loading : Hike()
    object Success : Hike()
    data class Error(val throwable: Throwable? = null) : Hike()

    companion object {
        @JvmStatic
        fun Hike.loading(): Boolean {
            return this is Loading
        }

        @JvmStatic
        fun Hike.completedSuccessful(): Boolean {
            return this is Success
        }

        @JvmStatic
        fun Hike.completedWithError(): Boolean {
            return this is Error
        }
    }
}