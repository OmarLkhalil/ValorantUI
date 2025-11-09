package com.larryyu.di

import com.larryyu.data.datasource.AgentsDataSource
import com.larryyu.data.datasource.AgentsDataSourceImpl
import com.larryyu.data.datasource.AgentsEndPoint
import com.larryyu.data.repository.AgentsRepoImpl
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.usecase.AgentsUseCase
import com.larryyu.domain.usecase.AgentsUseCaseImpl
import com.larryyu.ui.viewmodel.AgentsViewModel
import com.larryyu.ui.viewmodel.AgentDetailsViewModel
import com.larryyu.ui.viewmodel.GunsViewModel
import com.larryyu.db.ValorantDatabase
import io.ktor.client.HttpClient
import moe.tlaster.precompose.viewmodel.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

//val appModule = module {
//
//    // Provide HttpClient (NetworkModule.provideKtorClient uses Java 8 time API, ensure min API)
//    single<HttpClient> {
//        NetworkModule.provideKtorClient()
//    }
//
//    // AgentsEndPoint
//    single { AgentsEndPoint(get()) }
//
//    // DataSource
//    single<AgentsDataSource> { AgentsDataSourceImpl(get()) }
////
//    // SQLDelight driver and DB
////    single<SqlDriver> {AndroidSqliteDriver(
////        schema = ValorantDatabase.Schema,
////        context = androidContext(),
////        name = "ValorantDatabase.db"
////    )
////    }
//    single { ValorantDatabase(get()) }
//
//    // Repository
//    single<AgentsRepo> { AgentsRepoImpl(get(), get()) }
//
//    // Use cases
//    single<AgentsUseCase> { AgentsUseCaseImpl(get()) }
////    single<AgentDetailsUseCase> { AgentDetailsUseCaseImpl(get()) }
//
//    // ViewModels
//    singleOf(  ::AgentsViewModel )
//    singleOf(  ::AgentDetailsViewModel )
//    singleOf(  ::GunsViewModel )
//
//}

