package com.larryyu.domain.usecase

import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow

class GetAgentsUseCase(
    private val repository: AgentsRepo
) {
    suspend operator fun invoke(): Flow<DataState<BaseResponse<List<AgentsModel>>>> {
        return repository.getAgents()
    }
}
