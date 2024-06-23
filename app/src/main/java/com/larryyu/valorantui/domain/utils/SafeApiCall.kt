package com.larryyu.valorantui.domain.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException


const val NETWORK_TIMEOUT: Long = 6000000000

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Flow<DataState<T>> = flow {

    withTimeout(NETWORK_TIMEOUT) {
        val response = apiCall.invoke()
        emit(handleSuccess(response))
    }

}.onStart { emit(DataState.Loading) }.catch { emit(handleError(it)) }.flowOn(Dispatchers.IO)


fun <T> handleSuccess(response: T): DataState<T> {
    if (response != null) return DataState.Success(response)

    return DataState.Error(Exception("Response is null"))
}


fun <T> handleError(it: Throwable): DataState<T> {
    it.printStackTrace()
    return when (it) {
        is TimeoutCancellationException -> {
            DataState.Error(Exception("Timeout"))
        }

        is UnknownHostException -> {
            DataState.Error(Exception("No Internet"))
        }

        is IOException -> {
            DataState.Error(Exception("Network Error"))
        }

        is HttpException -> {
            DataState.Error(convertErrorBody(it))
        }

        else -> {
            DataState.Error(Exception("Unknown Error"))
        }
    }

}

fun convertErrorBody(throwable: HttpException): Exception {

    return when (throwable.code()) {
        401, 419 -> {
            Exception("Unauthorized")
        }

        404 -> {
            Exception("Not Found")
        }

        500 -> {
            Exception("Server Error")
        }

        else -> {
            Exception(throwable.message)
        }
    }
}