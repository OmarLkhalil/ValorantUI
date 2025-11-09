package com.larryyu.data.datasource

import com.larryyu.data.model.AgentsResponseBody
import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.utils.RetrofitConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AgentsEndPoint(
    private val client: HttpClient
) {
    suspend fun getAgents(): BaseResponse<List<AgentsResponseBody>> {
        return client.get("${RetrofitConstants.BASE_URL}agents").body()
    }

    suspend fun getAgentDetails(agentId: String): BaseResponse<AgentDetailsData> {
        return client.get("${RetrofitConstants.BASE_URL}agents/$agentId").body()
    }
}
