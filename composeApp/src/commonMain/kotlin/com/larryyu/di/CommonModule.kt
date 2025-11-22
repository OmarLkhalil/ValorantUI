package com.larryyu.di
import com.larryyu.data.datasource.AgentsDataSource
import com.larryyu.data.datasource.AgentsDataSourceImpl
import com.larryyu.data.datasource.AgentsEndPoint
import com.larryyu.data.datasource.WeaponsEndPoint
import com.larryyu.data.datasource.local.ThemeDataStore
import com.larryyu.data.repository.AgentsRepoImpl
import com.larryyu.data.repository.GunsRepoImpl
import com.larryyu.data.repository.PreferencesRepoimpl
import com.larryyu.db.ValorantDatabase
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.repository.GunsRepo
import com.larryyu.domain.repository.PreferencesRepo
import com.larryyu.domain.usecase.AgentDetailsUseCase
import com.larryyu.domain.usecase.AgentsUseCase
import com.larryyu.domain.usecase.AgentsUseCaseImpl
import com.larryyu.domain.usecase.GetAllBundlesUseCase
import com.larryyu.domain.usecase.GetAllGunsUseCase
import com.larryyu.domain.usecase.GetThemeUseCase
import com.larryyu.domain.usecase.SetThemeUseCase
import com.larryyu.presentation.viewmodel.AgentDetailsViewModel
import com.larryyu.presentation.viewmodel.AgentsViewModel
import com.larryyu.presentation.viewmodel.GunsViewModel
import com.larryyu.ui.theme.ThemeViewModel
import com.larryyu.utils.createDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
fun commonModule(enableNetworkLogs: Boolean = false) = module {
    single {
        NetworkModule.provideKtorClient(enableNetworkLogs = enableNetworkLogs)
    }
    single { AgentsEndPoint(get()) }
    single { WeaponsEndPoint(get()) }
    single<AgentsDataSource> { AgentsDataSourceImpl(get()) }
    single { ValorantDatabase(get()) }
    single<AgentsRepo> { AgentsRepoImpl(get(), get()) }
    single<GunsRepo> { GunsRepoImpl(get(), get()) }
    single<AgentsUseCase> { AgentsUseCaseImpl(get()) }
    factory { AgentDetailsUseCase(get()) }
    factory { GetAllBundlesUseCase(get()) }
    factory { GetAllGunsUseCase(get()) }
    singleOf(::AgentsViewModel)
    singleOf(::AgentDetailsViewModel)
    singleOf(::GunsViewModel)
    single { createDataStore() }
    single { ThemeDataStore(get()) }
    single<PreferencesRepo> { PreferencesRepoimpl(get()) }
    factory { GetThemeUseCase(get()) }
    factory { SetThemeUseCase(get()) }
    singleOf(::ThemeViewModel)
}
expect fun platformModule(): Module
