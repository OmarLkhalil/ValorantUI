package com.larryyu.data.datasource
import com.larryyu.domain.model.AgentsResponseModel
import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.utils.RetrofitConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
class AgentsEndPoint(
    private val client: HttpClient
) {
    suspend fun getAgents(): BaseResponse<List<AgentsResponseModel>> {
        return client.get("${RetrofitConstants.BASE_URL}agents").body()
    }
    suspend fun getAgentDetails(agentId: String): BaseResponse<AgentDetailsData> {
        return client.get("${RetrofitConstants.BASE_URL}agents/$agentId").body()
    }
}
