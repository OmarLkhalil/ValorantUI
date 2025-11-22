package com.larryyu.domain.repository
import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
interface AgentsRepo {
    suspend fun getAgents(): Flow<DataState<BaseResponse<List<AgentsModel>>>>
    suspend fun getAgentDetails(id: String): Flow<DataState<BaseResponse<AgentDetailsData>>>
    suspend fun getAgentsFromLocalDatabase(): List<AgentsModel>
    suspend fun insertAgentsToLocalDatabase(agents: List<AgentsModel>)
}
