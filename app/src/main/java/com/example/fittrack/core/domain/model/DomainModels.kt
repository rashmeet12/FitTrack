package com.example.fittrack.core.domain.model

import java.time.LocalDate

data class User(
    val id: Long = 0,
    val name: String,
    val age: Int,
    val height: Float, // in cm
    val weight: Float, // in kg
    val gender: String,
    val fitnessGoal: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class WeightEntry(
    val id: Long = 0,
    val userId: Long,
    val weight: Float, // in kg
    val date: LocalDate,
    val note: String? = null
)

data class Exercise(
    val id: Long = 0,
    val name: String,
    val description: String,
    val targetMuscleGroup: String,
    val isCustom: Boolean = false,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val createdBy: Long? = null, // null means system exercise, otherwise points to userId
    val createdAt: Long
)

data class Workout(
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val description: String? = null,
    val duration: Int = 0, // in minutes
    val caloriesBurned: Int? = null,
    val date: LocalDate,
    val startTime: Long,
    val endTime: Long? = null,
    val isCompleted: Boolean = false,
    val exercises: List<WorkoutExercise> = emptyList()
)

data class WorkoutExercise(
    val id: Long = 0,
    val workoutId: Long,
    val exercise: Exercise,
    val orderIndex: Int, // position in workout
    val targetSets: Int,
    val targetReps: Int? = null,
    val targetDuration: Int? = null, // in seconds, for timed exercises
    val restBetweenSets: Int? = null, // in seconds
    val sets: List<ExerciseSet> = emptyList()
)

data class ExerciseSet(
    val id: Long = 0,
    val workoutExerciseId: Long,
    val setNumber: Int,
    val reps: Int? = null,
    val weight: Float? = null, // in kg
    val duration: Int? = null, // in seconds
    val completed: Boolean = false,
    val timestamp: Long
)

data class WorkoutRoutine(
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val description: String? = null,
    val frequency: Int? = null, // target sessions per week
    val createdAt: Long,
    val exercises: List<RoutineExercise> = emptyList()
)

data class RoutineExercise(
    val id: Long = 0,
    val routineId: Long,
    val exercise: Exercise,
    val orderIndex: Int,
    val targetSets: Int,
    val targetReps: Int? = null,
    val targetDuration: Int? = null, // in seconds
    val restBetweenSets: Int? = null // in seconds
)

data class WorkoutStatistics(
    val totalWorkouts: Int,
    val totalDuration: Int, // in minutes
    val totalCaloriesBurned: Int,
    val mostFrequentExercise: String,
    val strongestLift: Pair<String, Float>, // exercise name, weight
    val workoutsThisWeek: Int,
    val workoutsLastWeek: Int,
    val averageWorkoutDuration: Int // in minutes
)