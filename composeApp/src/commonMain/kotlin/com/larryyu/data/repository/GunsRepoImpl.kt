package com.larryyu.data.repository

import com.larryyu.data.datasource.WeaponsEndPoint
import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import com.larryyu.domain.repository.GunsRepo
import com.larryyu.domain.utils.DataState
import com.larryyu.domain.utils.safeApiCall
import kotlinx.coroutines.flow.Flow


class GunsRepoImpl(
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