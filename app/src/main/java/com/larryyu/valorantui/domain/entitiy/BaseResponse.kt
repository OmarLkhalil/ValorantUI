package com.larryyu.valorantui.domain.entitiy

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class BaseResponse <T>(

    @Json(name="data")
    val data: T? = null,

    @Json(name="status")
    val status: Int? = null

) : Serializable