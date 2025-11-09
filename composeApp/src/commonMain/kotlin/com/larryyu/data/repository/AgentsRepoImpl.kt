package com.larryyu.data.repository

import com.larryyu.data.datasource.AgentsDataSource
import com.larryyu.data.mapper.toDomainModel
import com.larryyu.db.ValorantDatabase
import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.model.Role
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import com.larryyu.domain.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AgentsRepoImpl(
    private val remoteDataSource: AgentsDataSource,
    database: ValorantDatabase
) : AgentsRepo {
    private val queries = database.agentsQueries
    private val TAG = "AgentsRepoImpl"

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
            val rawRemote = remoteDataSource.getAgents()

            val remoteResponse: BaseResponse<List<AgentsModel>> = rawRemote.data.orEmpty().map { it.toDomainModel() }
                .let { BaseResponse(data = it, status = 200) }
            val remoteAgents = remoteResponse.data ?: emptyList()


            queries.clearAgents()

            remoteAgents.forEach {
                queries.insertAgent(
                    it.uuid.toString(),
                    it.displayName,
                    it.fullPortrait,
                    it.role?.displayName,
                    it.fullPortraitV2
                )
            }

            emit(DataState.Success(BaseResponse(data = remoteAgents, status = 200)))
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
            remoteDataSource.getAgentDetails(id)
        }
    }

    override suspend fun getAgentsFromLocalDatabase(): List<AgentsModel> {
        val local = queries.selectAllAgents { uuid, displayName, fullPortrait, roleDisplayName, fullPortraitV2 ->
            AgentsModel(
                uuid = uuid,
                displayName = displayName,
                fullPortrait = fullPortrait,
                role = roleDisplayName?.let { Role(it) },
                fullPortraitV2 = fullPortraitV2
            )
        }.executeAsList()
        return local
    }

    override suspend fun insertAgentsToLocalDatabase(agents: List<AgentsModel>) {
        queries.clearAgents()
        agents.forEach {
            queries.insertAgent(
                it.uuid,
                it.displayName,
                it.fullPortrait,
                it.role?.displayName,
                it.fullPortraitV2
            )
        }
    }

}