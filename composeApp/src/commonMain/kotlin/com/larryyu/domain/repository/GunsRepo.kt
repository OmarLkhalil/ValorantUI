package com.larryyu.domain.repository

import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow


interface GunsRepo {

    suspend fun getAllGuns () : Flow<DataState<BaseResponse<List<GunsData>>>>

    suspend fun getAllBundles () : Flow<DataState<BaseResponse<List<BundlesData>>>>
}