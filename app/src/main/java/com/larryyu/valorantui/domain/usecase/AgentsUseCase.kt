package com.larryyu.valorantui.domain.usecase

import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.AgentsData
import com.larryyu.valorantui.domain.repository.AgentsRepo
import com.larryyu.valorantui.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AgentsUseCase @Inject constructor(
    private val repo : AgentsRepo
) {
    suspend operator fun invoke() :  Flow<DataState<BaseResponse<List<AgentsData>>>> = repo.getAgents()
}