package com.larryyu.valorantui.domain.usecase

import com.larryyu.valorantui.domain.model.AgentDetailsData
import com.larryyu.valorantui.domain.repository.AgentsRepo
import com.larryyu.valorantui.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class AgentDetailsUseCase  @Inject constructor(
    private val agentsRepo: AgentsRepo
){
    suspend operator fun invoke(agentId: String) : Flow<DataState<AgentDetailsData>> = agentsRepo.getAgentDetails(agentId)
}