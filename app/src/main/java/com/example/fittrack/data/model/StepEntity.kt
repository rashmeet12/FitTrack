package com.example.fittrack.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val userId: String,
    @ColumnInfo val timestamp: Long, // epoch ms for the day
    @ColumnInfo val count: Int
)