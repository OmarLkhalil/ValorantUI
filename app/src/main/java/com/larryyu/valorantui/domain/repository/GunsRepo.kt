package com.larryyu.valorantui.domain.repository

import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.BundlesData
import com.larryyu.valorantui.domain.model.GunsData
import com.larryyu.valorantui.domain.utils.DataState
import kotlinx.coroutines.flow.Flow


interface GunsRepo {

    suspend fun getAllGuns () : Flow<DataState<BaseResponse<List<GunsData>>>>

    suspend fun getAllBundles () : Flow<DataState<BaseResponse<List<BundlesData>>>>
}