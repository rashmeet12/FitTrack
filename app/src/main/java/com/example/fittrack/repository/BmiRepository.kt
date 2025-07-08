package com.example.fittrack.repository

import com.example.fittrack.data.dao.BmiDao
import com.example.fittrack.data.model.BmiEntity
import javax.inject.Inject

class BmiRepository @Inject constructor(private val bmiDao: BmiDao) {
    suspend fun addBmi(userId: String, weight: Double, height: Double, bmi: Double, time: Long) {
        val bmiRecord = BmiEntity(userId = userId, weightKg = weight, heightM = height, bmiValue = bmi, timestamp = time)
        bmiDao.insert(bmiRecord)
    }
    suspend fun getBmiRecords(userId: String): List<BmiEntity> = bmiDao.getBmiForUser(userId)
}