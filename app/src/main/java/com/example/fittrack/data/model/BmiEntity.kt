package com.example.fittrack.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bmi_records")
data class BmiEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val userId: String,
    @ColumnInfo val weightKg: Double,
    @ColumnInfo val heightM: Double,
    @ColumnInfo val bmiValue: Double,
    @ColumnInfo val timestamp: Long // epoch ms
)