package com.larryyu.domain.usecase

import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow

class AgentDetailsUseCase(
    private val agentsRepo: AgentsRepo
){
    suspend operator fun invoke(agentId: String) : Flow<DataState<BaseResponse<AgentDetailsData>>> = agentsRepo.getAgentDetails(agentId)
}