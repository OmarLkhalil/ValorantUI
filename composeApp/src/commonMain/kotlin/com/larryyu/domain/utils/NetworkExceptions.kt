package com.larryyu.domain.utils
sealed class NetworkExceptions : Exception() {
    data object UnknownException : NetworkExceptions()
    data object ServerException : NetworkExceptions()
    data object NotFoundException : NetworkExceptions()
    data object TimeoutException : NetworkExceptions()
    data class UnProcessableContent(val msg: String) : NetworkExceptions()
    data object ConnectionException : NetworkExceptions()
    data object AuthorizationException : NetworkExceptions()
    data class CustomException(val msg: String) : NetworkExceptions()
    data class NeedActiveException(val msg: String) : NetworkExceptions()
    data object ForbiddenException : NetworkExceptions()
}
