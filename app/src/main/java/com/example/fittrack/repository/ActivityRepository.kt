package com.example.fittrack.repository

import com.example.fittrack.data.dao.ActivityDao
import com.example.fittrack.data.model.ActivityEntity
import javax.inject.Inject

class ActivityRepository @Inject constructor(private val activityDao: ActivityDao) {
    suspend fun addActivity(userId: String, name: String, start: Long, end: Long) {
        val activity = ActivityEntity(userId = userId, name = name, startTime = start, endTime = end)
        activityDao.insert(activity)
    }
    suspend fun getActivities(userId: String): List<ActivityEntity> = activityDao.getActivitiesForUser(userId)

    suspend fun deleteActivity(activityId: Long) {

       activityDao.deleteActivity(activityId)
        // If using other database: implement accordingly
    }
}