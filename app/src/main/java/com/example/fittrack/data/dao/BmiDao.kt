package com.example.fittrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fittrack.data.model.BmiEntity

@Dao
interface BmiDao {
    @Insert
    suspend fun insert(bmi: BmiEntity)
    @Query("SELECT * FROM bmi_records WHERE userId = :uid ORDER BY timestamp DESC")
    suspend fun getBmiForUser(uid: String): List<BmiEntity>
}