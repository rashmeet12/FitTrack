package com.example.fittrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fittrack.data.model.RouteSessionEntity

@Dao
interface RouteDao {
    @Insert
    suspend fun insert(route: RouteSessionEntity)
    @Query("SELECT * FROM route_sessions WHERE userId = :uid ORDER BY startTime DESC")
    suspend fun getRoutesForUser(uid: Long): List<RouteSessionEntity>
}