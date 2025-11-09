package com.larryyu.presentation.viewmodel

import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.usecase.AgentDetailsUseCase
import com.larryyu.presentation.uistates.AgentDetailsIntent
import com.larryyu.presentation.uistates.AgentDetailsState


class AgentDetailsViewModel(
    private val agentDetailsUseCase: AgentDetailsUseCase,
) : BaseViewModel<AgentDetailsIntent, AgentDetailsState>(AgentDetailsState()) {

    override suspend fun handleIntent(intent: AgentDetailsIntent) {
        super.handleIntent(intent)
        when (intent) {
            is AgentDetailsIntent.FetchAgentDetails -> fetchAgentDetails(intent.agentId)
        }
    }

    private suspend fun fetchAgentDetails(agentId: String) {
        collectDataState(agentDetailsUseCase(agentId)) { response ->
            setState {
                copy(
                    agentDetails = response.data ?: AgentDetailsData(),
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    override fun onLoading() {
        super.onLoading()
        setState { copy(isLoading = true, error = null) }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        setState { copy(isLoading = false, error = e.message) }
    }

    override fun onIdle() {
        super.onIdle()
        setState { copy(isLoading = false, error = null) }
    }
}