package com.larryyu.valorantui.data.repositoryimp

import com.larryyu.valorantui.data.api.AgentsEndPoint
import com.larryyu.valorantui.domain.model.AgentDetailsData
import com.larryyu.valorantui.domain.model.AgentsData
import com.larryyu.valorantui.domain.repository.AgentsRepo
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.domain.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AgentsRepoImpl @Inject constructor(
    private val api : AgentsEndPoint
) : AgentsRepo {
    override suspend fun getAgents(): Flow<DataState<AgentsData>> {
        return safeApiCall {
            api.getAgents()
        }
    }

    override suspend fun getAgentDetails(id: String): Flow<DataState<AgentDetailsData>> {
        return safeApiCall {
            api.getAgentDetails(id)
        }
    }
}