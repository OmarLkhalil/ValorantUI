package com.larryyu.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val data: T? = null,
    val status: Int? = null
)
