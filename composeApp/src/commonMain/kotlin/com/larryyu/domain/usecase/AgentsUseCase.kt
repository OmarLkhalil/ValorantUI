package com.larryyu.domain.usecase

import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow


interface AgentsUseCase {
    suspend operator fun invoke(): Flow<DataState<BaseResponse<List<AgentsModel>>>>
}


class AgentsUseCaseImpl(
    private val repo: AgentsRepo
) : AgentsUseCase {

    override suspend fun invoke(): Flow<DataState<BaseResponse<List<AgentsModel>>>> {
        return repo.getAgents()
    }

}