package com.larryyu.valorantui.di

import com.larryyu.valorantui.data.api.WeaponsEndPoint
import com.larryyu.valorantui.data.repositoryimp.GunsRepoImpl
import com.larryyu.valorantui.domain.repository.GunsRepo
import com.larryyu.valorantui.domain.usecase.GetAllBundlesUseCase
import com.larryyu.valorantui.domain.usecase.GetAllGunsUseCase
import com.larryyu.valorantui.ui.model.BundlesReducer
import com.larryyu.valorantui.ui.model.GunsReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GunsModule {


    @Singleton
    @Provides
    fun provideGunsRepo(
        api: WeaponsEndPoint
    ): GunsRepo = GunsRepoImpl(
        api
    )


    @Singleton
    @Provides
    fun provideWeaponsEndPoint(
        retrofit: Retrofit
    ): WeaponsEndPoint {
        return retrofit.create(WeaponsEndPoint::class.java)
    }

    @Singleton
    @Provides
    fun provideWeaponsUseCase(
        repo: GunsRepo
    ): GetAllGunsUseCase {
        return GetAllGunsUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetAllBundles(
        repo: GunsRepo
    ): GetAllBundlesUseCase {
        return GetAllBundlesUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGunsReducer(): GunsReducer {
        return GunsReducer()
    }

    @Provides
    @Singleton
    fun provideBundlesReducer(): BundlesReducer {
        return BundlesReducer()
    }
}