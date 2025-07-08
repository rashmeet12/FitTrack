package com.example.fittrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fittrack.data.model.ActivityEntity

@Dao
interface ActivityDao {
    @Insert
    suspend fun insert(activity: ActivityEntity)
    @Query("SELECT * FROM activities WHERE userId = :uid ORDER BY startTime DESC")
    suspend fun getActivitiesForUser(uid: String): List<ActivityEntity>
    @Query("DELETE FROM activities WHERE id = :activityId")
    suspend fun deleteActivity(activityId: Long)
}