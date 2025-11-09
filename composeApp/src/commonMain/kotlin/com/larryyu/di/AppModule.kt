package com.larryyu.di

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

