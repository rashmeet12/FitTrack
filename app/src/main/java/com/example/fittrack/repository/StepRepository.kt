package com.example.fittrack.repository

import com.example.fittrack.data.dao.StepDao
import com.example.fittrack.data.model.StepEntity
import javax.inject.Inject

class StepRepository @Inject constructor(private val stepDao: StepDao) {
    suspend fun addSteps(userId: String, timestamp: Long, count: Int) {
        stepDao.insert(StepEntity(userId = userId, timestamp = timestamp, count = count))
    }
    suspend fun getSteps(userId: String): List<StepEntity> = stepDao.getStepsForUser(userId)
}
