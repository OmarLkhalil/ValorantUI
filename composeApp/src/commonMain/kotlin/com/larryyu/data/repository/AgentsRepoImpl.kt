package com.larryyu.data.repository

import com.larryyu.data.datasource.AgentsEndPoint
import com.larryyu.db.ValorantDatabase
import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsResponseModel as AgentsModel
import com.larryyu.domain.model.AgentsRoleModel as Role
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import com.larryyu.domain.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AgentsRepoImpl(
    private val endPoint: AgentsEndPoint,
    database: ValorantDatabase
) : AgentsRepo {

    private val queries = database.agentsQueries

    override suspend fun getAgents(): Flow<DataState<BaseResponse<List<AgentsModel>>>> = flow {
        emit(DataState.Loading)

        val cachedAgents = queries.selectAllAgents { uuid, displayName, fullPortrait, roleDisplayName, fullPortraitV2 ->
            AgentsModel(
                uuid = uuid,
                displayName = displayName,
                fullPortrait = fullPortrait,
                role = roleDisplayName?.let { Role(it) },
                fullPortraitV2 = fullPortraitV2
            )
        }.executeAsList()

        if (cachedAgents.isNotEmpty()) {
            emit(DataState.Success(BaseResponse(data = cachedAgents, status = 200)))
        }

        try {
            val remoteResponse = endPoint.getAgents()
            val remoteAgents = remoteResponse.data.orEmpty()

            queries.clearAgents()
            remoteAgents.forEach { agent ->
                queries.insertAgent(
                    agent.uuid ?: "",
                    agent.displayName ?: "",
                    agent.fullPortrait ?: "",
                    agent.role?.displayName,
                    agent.fullPortraitV2 ?: ""
                )
            }

            emit(DataState.Success(BaseResponse(data = remoteAgents, status = remoteResponse.status)))
        } catch (e: Exception) {
            if (cachedAgents.isEmpty()) {
                emit(DataState.Error(e))
            } else {
                emit(DataState.Success(BaseResponse(data = cachedAgents, status = 200)))
            }
        }
    }

    override suspend fun getAgentDetails(id: String): Flow<DataState<BaseResponse<AgentDetailsData>>> {
        return safeApiCall {
            endPoint.getAgentDetails(id)
        }
    }

    override suspend fun getAgentsFromLocalDatabase(): List<AgentsModel> {
        return queries.selectAllAgents { uuid, displayName, fullPortrait, roleDisplayName, fullPortraitV2 ->
            AgentsModel(
                uuid = uuid,
                displayName = displayName,
                fullPortrait = fullPortrait,
                role = roleDisplayName?.let { Role(it) },
                fullPortraitV2 = fullPortraitV2
            )
        }.executeAsList()
    }

    override suspend fun insertAgentsToLocalDatabase(agents: List<AgentsModel>) {
        queries.clearAgents()
        agents.forEach { agent ->
            queries.insertAgent(
                agent.uuid ?: "",
                agent.displayName ?: "",
                agent.fullPortrait ?: "",
                agent.role?.displayName,
                agent.fullPortraitV2 ?: ""
            )
        }
    }
}

