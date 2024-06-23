package com.larryyu.valorantui.domain.repository

import com.larryyu.valorantui.domain.model.AgentDetailsData
import com.larryyu.valorantui.domain.model.AgentsData
import com.larryyu.valorantui.domain.utils.DataState
import kotlinx.coroutines.flow.Flow

interface AgentsRepo {
    suspend fun getAgents() : Flow<DataState<AgentsData>>
    suspend fun getAgentDetails(id: String) : Flow<DataState<AgentDetailsData>>
}