package com.larryyu.domain.utils
sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data object Idle : DataState<Nothing>()
}
