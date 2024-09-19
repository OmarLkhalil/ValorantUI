package com.larryyu.valorantui.data.repositoryimp

import com.larryyu.valorantui.data.api.WeaponsEndPoint
import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.BundlesData
import com.larryyu.valorantui.domain.model.GunsData
import com.larryyu.valorantui.domain.repository.GunsRepo
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.domain.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GunsRepoImpl @Inject constructor(
    private val api: WeaponsEndPoint
) : GunsRepo {

    override suspend fun getAllGuns(): Flow<DataState<BaseResponse<List<GunsData>>>> {
        return safeApiCall {
            api.getAllGuns()
        }
    }

    override suspend fun getAllBundles(): Flow<DataState<BaseResponse<List<BundlesData>>>> {
        return safeApiCall {
            api.getAllBundles()
        }
    }
}