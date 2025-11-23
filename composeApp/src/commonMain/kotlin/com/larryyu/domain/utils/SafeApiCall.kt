package com.larryyu.domain.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeout
import kotlinx.io.IOException
const val NETWORK_TIMEOUT: Long = 6000000000
fun <T> safeApiCall(
    apiCall: suspend () -> T
): Flow<DataState<T>> = flow {
    withTimeout(NETWORK_TIMEOUT) {
        val response = apiCall.invoke()
        emit(handleSuccess(response))
    }
}.onStart {
    emit(DataState.Loading)
}.catch {
    emit(handleError(it))
}.flowOn(Dispatchers.Default)
fun <T> handleSuccess(response: T): DataState<T> {
    if (response != null) return DataState.Success(response)
    return DataState.Error(NetworkExceptions.UnknownException)
}
fun <T> handleError(it: Throwable): DataState<T> {
    // Log error (printStackTrace is not available in Kotlin/Native)
    println("Error occurred: ${it.message}")
    return when (it) {
        is TimeoutCancellationException -> {
            DataState.Error(NetworkExceptions.TimeoutException)
        }
        is IOException -> {
            DataState.Error(NetworkExceptions.ConnectionException)
        }
        else -> {
            val message = it.message?.lowercase() ?: ""
            if (message.contains("host") || message.contains("network") || message.contains("connect")) {
                DataState.Error(NetworkExceptions.ConnectionException)
            } else {
                DataState.Error(NetworkExceptions.UnknownException)
            }
        }
    }
}
