package com.example.fittrack.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val userId: String,
    @ColumnInfo val name: String,
    @ColumnInfo val startTime: Long,
    @ColumnInfo val endTime: Long
)