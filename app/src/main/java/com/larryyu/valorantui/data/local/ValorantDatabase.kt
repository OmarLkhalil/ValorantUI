package com.larryyu.valorantui.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.larryyu.valorantui.domain.model.AgentsData


@Database(entities = [AgentsData::class], version = 1, exportSchema = false)
abstract class ValorantDatabase : RoomDatabase() {
    abstract fun agentsDao(): AgentsDao
}