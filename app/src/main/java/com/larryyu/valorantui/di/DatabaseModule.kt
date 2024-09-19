package com.larryyu.valorantui.di

import android.content.Context
import androidx.room.Room
import com.larryyu.valorantui.data.local.ValorantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context : Context) : ValorantDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            ValorantDatabase::class.java,
            "valorantDatabase"
        ).build()
    }
}