package com.larryyu.data.datasource
import com.larryyu.data.model.AgentsResponseBody
import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
interface AgentsDataSource {
    suspend fun getAgents(): BaseResponse<List<AgentsResponseBody>>
    suspend fun getAgentDetails(agentId: String): BaseResponse<AgentDetailsData>
}
internal class AgentsDataSourceImpl(
    private val service: AgentsEndPoint
) : AgentsDataSource {
    override suspend fun getAgents(): BaseResponse<List<AgentsResponseBody>> {
        return service.getAgents()
    }
    override suspend fun getAgentDetails(agentId: String): BaseResponse<AgentDetailsData> {
        return service.getAgentDetails(agentId)
    }
}
