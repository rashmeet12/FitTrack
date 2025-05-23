package com.example.fittrack.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fittrack.core.database.dao.ExerciseDao
import com.example.fittrack.core.database.dao.ExerciseSetDao
import com.example.fittrack.core.database.dao.RoutineExerciseDao
import com.example.fittrack.core.database.dao.UserDao
import com.example.fittrack.core.database.dao.WeightEntryDao
import com.example.fittrack.core.database.dao.WorkoutDao
import com.example.fittrack.core.database.dao.WorkoutExerciseDao
import com.example.fittrack.core.database.dao.WorkoutRoutineDao
import com.example.fittrack.core.database.entity.ExerciseEntity
import com.example.fittrack.core.database.entity.ExerciseSetEntity
import com.example.fittrack.core.database.entity.RoutineExerciseEntity
import com.example.fittrack.core.database.entity.UserEntity
import com.example.fittrack.core.database.entity.WeightEntryEntity
import com.example.fittrack.core.database.entity.WorkoutEntity
import com.example.fittrack.core.database.entity.WorkoutExerciseEntity
import com.example.fittrack.core.database.entity.WorkoutRoutineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        WeightEntryEntity::class,
        ExerciseEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        ExerciseSetEntity::class,
        WorkoutRoutineEntity::class,
        RoutineExerciseEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FitTrackDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun exerciseSetDao(): ExerciseSetDao
    abstract fun workoutRoutineDao(): WorkoutRoutineDao
    abstract fun routineExerciseDao(): RoutineExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: FitTrackDatabase? = null

        fun getDatabase(context: Context): FitTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitTrackDatabase::class.java,
                    "fittrack_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate database with default exercises
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    prepopulateDatabase(database.exerciseDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateDatabase(exerciseDao: ExerciseDao) {
            val currentTime = System.currentTimeMillis()

            // List of default exercises
            val defaultExercises = listOf(
                ExerciseEntity(
                    name = "Bench Press",
                    description = "A compound exercise that targets the chest, shoulders, and triceps.",
                    targetMuscleGroup = "Chest",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Squat",
                    description = "A compound exercise that targets the quadriceps, hamstrings, and glutes.",
                    targetMuscleGroup = "Legs",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Deadlift",
                    description = "A compound exercise that targets the back, glutes, and hamstrings.",
                    targetMuscleGroup = "Back",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Pull-up",
                    description = "A bodyweight exercise that targets the back and biceps.",
                    targetMuscleGroup = "Back",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Push-up",
                    description = "A bodyweight exercise that targets the chest, shoulders, and triceps.",
                    targetMuscleGroup = "Chest",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Shoulder Press",
                    description = "A compound exercise that targets the shoulders and triceps.",
                    targetMuscleGroup = "Shoulders",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Bicep Curl",
                    description = "An isolation exercise that targets the biceps.",
                    targetMuscleGroup = "Arms",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Tricep Extension",
                    description = "An isolation exercise that targets the triceps.",
                    targetMuscleGroup = "Arms",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Leg Press",
                    description = "A machine exercise that targets the quadriceps, hamstrings, and glutes.",
                    targetMuscleGroup = "Legs",
                    isCustom = false,
                    createdAt = currentTime
                ),
                ExerciseEntity(
                    name = "Lat Pulldown",
                    description = "A machine exercise that targets the back and biceps.",
                    targetMuscleGroup = "Back",
                    isCustom = false,
                    createdAt = currentTime
                )
            )

            defaultExercises.forEach { exercise ->
                exerciseDao.insertExercise(exercise)
            }
        }
    }
}