package com.larryyu.di//package com.larryyu.di
//
//import com.larryyu.data.datasource.AgentsEndPoint
//import com.larryyu.data.datasource.WeaponsEndPoint
//import com.larryyu.data.repository.GunsRepoImpl
//import com.larryyu.domain.repository.GunsRepo
//import com.larryyu.domain.usecase.GetAllBundlesUseCase
//import com.larryyu.domain.usecase.GetAllGunsUseCase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import io.ktor.client.HttpClient
//import retrofit2.Retrofit
//import javax.inject.Singleton
//
//
//@Module
//@InstallIn(SingletonComponent::class)
//object GunsModule {
//
//
//    @Singleton
//    @Provides
//    fun provideGunsRepo(
//        api: WeaponsEndPoint
//    ): GunsRepo = GunsRepoImpl(
//        api
//    )
//
//
//
//    @Provides
//    @Singleton
//    fun provideGunsEndPoint(client: HttpClient): WeaponsEndPoint {
//        return WeaponsEndPoint(client)
//    }
//
//    @Singleton
//    @Provides
//    fun provideWeaponsUseCase(
//        repo: GunsRepo
//    ): GetAllGunsUseCase {
//        return GetAllGunsUseCase(repo)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGetAllBundles(
//        repo: GunsRepo
//    ): GetAllBundlesUseCase {
//        return GetAllBundlesUseCase(repo)
//    }
//}