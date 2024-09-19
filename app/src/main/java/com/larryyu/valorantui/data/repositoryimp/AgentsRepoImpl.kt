package com.larryyu.valorantui.data.repositoryimp

import com.larryyu.valorantui.data.api.AgentsEndPoint
import com.larryyu.valorantui.data.local.ValorantDatabase
import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.AgentDetailsData
import com.larryyu.valorantui.domain.model.AgentsData
import com.larryyu.valorantui.domain.repository.AgentsRepo
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.domain.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class AgentsRepoImpl @Inject constructor(
    private val api: AgentsEndPoint,
    private val valDatabase: ValorantDatabase
) : AgentsRepo {

    override suspend fun getAgents(): Flow<DataState<BaseResponse<List<AgentsData>>>> {
        return safeApiCall {
            api.getAgents()
        }
    }

    override suspend fun getAgentDetails(id: String): Flow<DataState<BaseResponse<AgentDetailsData>>> {
        return safeApiCall {
            api.getAgentDetails(id)
        }
    }

    override suspend fun getAgentsFromLocalDatabase(): List<AgentsData> {
        return valDatabase.agentsDao().getAllAgents()
    }

    override suspend fun insertAgentsToLocalDatabase(agents: List<AgentsData>) {
        valDatabase.agentsDao().insertAgents(agents)
    }
}