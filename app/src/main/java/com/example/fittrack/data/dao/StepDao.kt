package com.example.fittrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fittrack.data.model.StepEntity

@Dao
interface StepDao {
    @Insert
    suspend fun insert(step: StepEntity)
    @Query("SELECT * FROM steps WHERE userId = :uid ORDER BY timestamp DESC")
    suspend fun getStepsForUser(uid: String): List<StepEntity>
    @Query("SELECT * FROM steps WHERE userId = :uid AND timestamp BETWEEN :start AND :end")
    suspend fun getStepsInRange(uid: Long, start: Long, end: Long): List<StepEntity>
}