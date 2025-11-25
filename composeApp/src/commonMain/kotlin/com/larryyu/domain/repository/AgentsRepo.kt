package com.larryyu.domain.repository

import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsResponseModel
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow

interface AgentsRepo {
    suspend fun getAgents(): Flow<DataState<BaseResponse<List<AgentsResponseModel>>>>
    suspend fun getAgentDetails(id: String): Flow<DataState<BaseResponse<AgentDetailsData>>>
    suspend fun getAgentsFromLocalDatabase(): List<AgentsResponseModel>
    suspend fun insertAgentsToLocalDatabase(agents: List<AgentsResponseModel>)
}
