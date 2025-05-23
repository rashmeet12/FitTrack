package com.example.fittrack.core.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val name: String,
    val age: Int,
    val height: Float, // in cm
    val weight: Float, // in kg
    val gender: String,
    val fitnessGoal: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "weight_entries")
data class WeightEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val entryId: Long = 0,
    val userId: Long,
    val weight: Float, // in kg
    val date: Long,
    val note: String? = null
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Long = 0,
    val name: String,
    val description: String,
    val targetMuscleGroup: String,
    val isCustom: Boolean = false,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val createdBy: Long? = null, // null means system exercise, otherwise points to userId
    val createdAt: Long
)

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Long = 0,
    val userId: Long,
    val name: String,
    val description: String? = null,
    val duration: Int = 0, // in minutes
    val caloriesBurned: Int? = null,
    val date: Long,
    val startTime: Long,
    val endTime: Long? = null,
    val isCompleted: Boolean = false
)

@Entity(tableName = "workout_exercises")
data class WorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val workoutExerciseId: Long = 0,
    val workoutId: Long,
    val exerciseId: Long,
    val orderIndex: Int, // position in workout
    val targetSets: Int,
    val targetReps: Int? = null,
    val targetDuration: Int? = null, // in seconds, for timed exercises
    val restBetweenSets: Int? = null // in seconds
)

@Entity(tableName = "exercise_sets")
data class ExerciseSetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Long = 0,
    val workoutExerciseId: Long,
    val setNumber: Int,
    val reps: Int? = null,
    val weight: Float? = null, // in kg
    val duration: Int? = null, // in seconds
    val completed: Boolean = false,
    val timestamp: Long
)

@Entity(tableName = "workout_routines")
data class WorkoutRoutineEntity(
    @PrimaryKey(autoGenerate = true)
    val routineId: Long = 0,
    val userId: Long,
    val name: String,
    val description: String? = null,
    val frequency: Int? = null, // target sessions per week
    val createdAt: Long
)

@Entity(tableName = "routine_exercises")
data class RoutineExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val routineExerciseId: Long = 0,
    val routineId: Long,
    val exerciseId: Long,
    val orderIndex: Int,
    val targetSets: Int,
    val targetReps: Int? = null,
    val targetDuration: Int? = null, // in seconds
    val restBetweenSets: Int? = null // in seconds
)