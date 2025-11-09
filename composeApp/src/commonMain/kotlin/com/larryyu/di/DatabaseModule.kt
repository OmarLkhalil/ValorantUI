package com.larryyu.di//package com.larryyu.di
//
//import android.content.Context
//import androidx.room.Room
//import com.larryyu.db.ValorantDatabase
////import com.larryyu.data.local.ValorantDatabase
//import com.squareup.sqldelight.android.AndroidSqliteDriver
//import com.squareup.sqldelight.db.SqlDriver
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//
////    @Singleton
////    @Provides
////    fun provideAppDatabase(@ApplicationContext context : Context) : ValorantDatabase{
////        return Room.databaseBuilder(
////            context.applicationContext,
////            ValorantDatabase::class.java,
////            "valorantDatabase"
////        ).build()
////    }
//
//    @Singleton
//    @Provides
//    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver {
//        return AndroidSqliteDriver(
//            ValorantDatabase.Schema,
//            context,
//            "valorantDatabase.db"
//        )
//    }
//
//    @Singleton
//    @Provides
//    fun provideDatabase(driver: SqlDriver): ValorantDatabase {
//        return ValorantDatabase(driver)
//    }
//}