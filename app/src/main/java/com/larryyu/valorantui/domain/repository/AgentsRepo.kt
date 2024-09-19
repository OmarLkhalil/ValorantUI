package com.larryyu.valorantui.domain.repository

import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.AgentDetailsData
import com.larryyu.valorantui.domain.model.AgentsData
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.ui.model.AgentDetailsState
import kotlinx.coroutines.flow.Flow


interface AgentsRepo {

    suspend fun getAgents(): Flow<DataState<BaseResponse<List<AgentsData>>>>

    suspend fun getAgentDetails(id: String): Flow<DataState<BaseResponse<AgentDetailsData>>>

    suspend fun getAgentsFromLocalDatabase(): List<AgentsData>

    suspend fun insertAgentsToLocalDatabase(agents: List<AgentsData>)
}