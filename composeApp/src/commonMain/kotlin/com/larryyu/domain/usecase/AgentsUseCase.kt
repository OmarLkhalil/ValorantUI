package com.larryyu.domain.usecase

import com.larryyu.domain.repository.AgentsRepo

class GetAgentsUseCase(
    private val repository: AgentsRepo
) {
    suspend operator fun invoke() = repository.getAgents()
}
