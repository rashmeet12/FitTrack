package com.example.fittrack.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_sessions")
data class RouteSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val userId: String,
    @ColumnInfo val startTime: Long,
    @ColumnInfo val endTime: Long,
    @ColumnInfo val coordinates: String // JSON of LatLng list
)
