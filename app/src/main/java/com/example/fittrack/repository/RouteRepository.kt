package com.example.fittrack.repository

import com.example.fittrack.data.dao.RouteDao
import com.example.fittrack.data.model.RouteSessionEntity
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RouteRepository @Inject constructor(private val routeDao: RouteDao) {
    suspend fun addRoute(userId: String, start: Long, end: Long, path: StateFlow<List<LatLng>>) {
        val jsonPath = Gson().toJson(path)
        val route = RouteSessionEntity(userId = userId, startTime = start, endTime = end, coordinates = jsonPath)
        routeDao.insert(route)
    }
    suspend fun getRoutes(userId: Long): List<RouteSessionEntity> = routeDao.getRoutesForUser(userId)
}