package com.larryyu.valorantui.di

import com.larryyu.valorantui.data.api.AgentsEndPoint
import com.larryyu.valorantui.data.repositoryimp.AgentsRepoImpl
import com.larryyu.valorantui.domain.repository.AgentsRepo
import com.larryyu.valorantui.domain.usecase.AgentDetailsUseCase
import com.larryyu.valorantui.domain.usecase.AgentsUseCase
import com.larryyu.valorantui.ui.model.AgentDetailsReducer
import com.larryyu.valorantui.ui.model.AgentsReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AgentsModule {

    @Provides
    @Singleton
    fun provideAgentsRepo(
        api: AgentsEndPoint
    ): AgentsRepo = AgentsRepoImpl(api)

    @Provides
    @Singleton
    fun provideAgentsEndPoint(retrofit: Retrofit): AgentsEndPoint {
        return retrofit.create(AgentsEndPoint::class.java)
    }

    @Provides
    @Singleton
    fun provideAgentsReducer(): AgentsReducer {
        return AgentsReducer()
    }

    @Provides
    @Singleton
    fun provideAgentDetailsReducer(): AgentDetailsReducer {
        return AgentDetailsReducer()
    }

    @Provides
    @Singleton
    fun provideAgentsUseCase(agentsRepo: AgentsRepo) = AgentsUseCase(agentsRepo)

    @Provides
    @Singleton
    fun provideDetailsUseCase(agentsRepo: AgentsRepo) = AgentDetailsUseCase(agentsRepo)

}