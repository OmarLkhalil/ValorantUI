package com.larryyu.valorantui.data.api

import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.BundlesData
import com.larryyu.valorantui.domain.model.GunsData
import retrofit2.http.GET



interface WeaponsEndPoint {

    @GET("weapons")
    suspend fun getAllGuns() : BaseResponse<List<GunsData>>

    @GET("bundles")
    suspend fun getAllBundles() : BaseResponse<List<BundlesData>>
}