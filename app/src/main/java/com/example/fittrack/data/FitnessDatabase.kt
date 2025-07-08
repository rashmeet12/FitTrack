package com.example.fittrack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fittrack.data.dao.ActivityDao
import com.example.fittrack.data.dao.BmiDao
import com.example.fittrack.data.dao.RouteDao
import com.example.fittrack.data.dao.StepDao
import com.example.fittrack.data.dao.UserDao
import com.example.fittrack.data.model.ActivityEntity
import com.example.fittrack.data.model.BmiEntity
import com.example.fittrack.data.model.RouteSessionEntity
import com.example.fittrack.data.model.StepEntity
import com.example.fittrack.data.model.UserEntity
import com.example.fittrack.data.utils.Converters

@Database(
    entities = [UserEntity::class, StepEntity::class, BmiEntity::class, RouteSessionEntity::class, ActivityEntity::class],
    version = 4, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun stepDao(): StepDao
    abstract fun bmiDao(): BmiDao
    abstract fun routeDao(): RouteDao
    abstract fun activityDao(): ActivityDao
}

