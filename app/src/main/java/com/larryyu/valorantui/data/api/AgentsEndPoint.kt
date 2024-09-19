package com.larryyu.valorantui.data.api

import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.AgentDetailsData
import com.larryyu.valorantui.domain.model.AgentsData
import retrofit2.http.GET
import retrofit2.http.Path


interface AgentsEndPoint {

    @GET("agents")
    suspend fun getAgents() : BaseResponse<List<AgentsData>>

    @GET("agents/{agentId}")
    suspend fun getAgentDetails(@Path ("agentId") agentId: String ) : BaseResponse<AgentDetailsData>

}