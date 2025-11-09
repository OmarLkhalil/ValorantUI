package com.larryyu.data.datasource

import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import com.larryyu.domain.utils.RetrofitConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get


class WeaponsEndPoint (
    private val client: HttpClient
) {
    suspend fun getAllGuns(): BaseResponse<List<GunsData>> {
        return client.get("${RetrofitConstants.BASE_URL}weapons").body()
    }

    suspend fun getAllBundles(): BaseResponse<List<BundlesData>> {
        return client.get("${RetrofitConstants.BASE_URL}bundles").body()
    }
}
