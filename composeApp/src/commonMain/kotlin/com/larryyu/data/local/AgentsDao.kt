package com.larryyu.data.local

//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.larryyu.domain.model.AgentsModel
//
//
//@Dao
//interface AgentsDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAgents(agents: List<AgentsModel>)
//
//    @Query("SELECT * FROM agents")
//    suspend fun getAllAgents(): List<AgentsModel>
//
//    @Query("DELETE FROM agents")
//    suspend fun clearAgents()
//}