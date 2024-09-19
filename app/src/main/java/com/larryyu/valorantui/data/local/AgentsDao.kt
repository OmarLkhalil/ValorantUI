package com.larryyu.valorantui.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.larryyu.valorantui.domain.model.AgentsData


@Dao
interface AgentsDao {

    @Query("SELECT * FROM agents")
    suspend fun getAllAgents(): List<AgentsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgents(agents: List<AgentsData>)
}