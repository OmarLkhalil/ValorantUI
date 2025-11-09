package com.larryyu.di

import com.larryyu.data.datasource.AgentsDataSource
import com.larryyu.data.datasource.AgentsDataSourceImpl
import com.larryyu.data.datasource.AgentsEndPoint
import com.larryyu.data.repository.AgentsRepoImpl
import com.larryyu.db.ValorantDatabase
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.usecase.AgentDetailsUseCase
import com.larryyu.domain.usecase.AgentsUseCase
import com.larryyu.domain.usecase.AgentsUseCaseImpl
import com.larryyu.presentation.viewmodel.AgentsViewModel
import com.larryyu.presentation.viewmodel.AgentDetailsViewModel
import com.larryyu.presentation.viewmodel.GunsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


fun commonModule(enableNetworkLogs: Boolean = false) = module {

    // HttpClient (provided by platformModule)
    single {
        NetworkModule.provideKtorClient(enableNetworkLogs = enableNetworkLogs)
    }
    // EndPoint
    single { AgentsEndPoint(get()) }

    // DataSource
    single<AgentsDataSource> { AgentsDataSourceImpl(get()) }

    // Repository
    single<AgentsRepo> { AgentsRepoImpl(get(), get()) }

    // UseCase
    single<AgentsUseCase> { AgentsUseCaseImpl(get()) }

    single { ValorantDatabase(get()) }

    factory { AgentDetailsUseCase(get()) }

    // ViewModels
    singleOf(::AgentsViewModel)
    singleOf(::AgentDetailsViewModel)
    singleOf(::GunsViewModel)

}

expect fun platformModule(): Module