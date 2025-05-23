package com.example.fittrack.core.domain.repository

import com.example.fittrack.core.domain.model.Exercise
import com.example.fittrack.core.domain.model.ExerciseSet
import com.example.fittrack.core.domain.model.RoutineExercise
import com.example.fittrack.core.domain.model.User
import com.example.fittrack.core.domain.model.WeightEntry
import com.example.fittrack.core.domain.model.Workout
import com.example.fittrack.core.domain.model.WorkoutExercise
import com.example.fittrack.core.domain.model.WorkoutRoutine
import com.example.fittrack.core.domain.model.WorkoutStatistics
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for User operations
 */
interface UserRepository {
    suspend fun createUser(user: User): Long
    suspend fun updateUser(user: User)
    fun getUserById(userId: Long): Flow<User?>
    fun getActiveUser(): Flow<User?>
    suspend fun deleteUser(userId: Long)
}

/**
 * Repository interface for weight tracking operations
 */
interface WeightRepository {
    suspend fun addWeightEntry(entry: WeightEntry): Long
    suspend fun updateWeightEntry(entry: WeightEntry)
    suspend fun deleteWeightEntry(entryId: Long)
    fun getWeightEntriesForUser(userId: Long): Flow<List<WeightEntry>>
    fun getWeightEntriesBetweenDates(userId: Long, startDate: LocalDate, endDate: LocalDate): Flow<List<WeightEntry>>
    fun getLatestWeightEntry(userId: Long): Flow<WeightEntry?>
}

/**
 * Repository interface for Exercise operations
 */
interface ExerciseRepository {
    suspend fun createExercise(exercise: Exercise): Long
    suspend fun updateExercise(exercise: Exercise)
    suspend fun deleteExercise(exerciseId: Long)
    fun getExerciseById(exerciseId: Long): Flow<Exercise?>
    fun getPresetExercises(): Flow<List<Exercise>>
    fun getCustomExercisesForUser(userId: Long): Flow<List<Exercise>>
    fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<Exercise>>
    fun searchExercises(query: String): Flow<List<Exercise>>
}

/**
 * Repository interface for Workout operations
 */
interface WorkoutRepository {
    suspend fun createWorkout(workout: Workout): Long
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(workoutId: Long)
    fun getWorkoutById(workoutId: Long): Flow<Workout?>
    fun getWorkoutsForUser(userId: Long): Flow<List<Workout>>
    fun getCompletedWorkoutsForUser(userId: Long): Flow<List<Workout>>
    fun getWorkoutsBetweenDates(userId: Long, startDate: LocalDate, endDate: LocalDate): Flow<List<Workout>>
    fun getRecentCompletedWorkouts(userId: Long, limit: Int): Flow<List<Workout>>
    suspend fun addExerciseToWorkout(workoutId: Long, workoutExercise: WorkoutExercise): Long
    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExercise)
    suspend fun removeExerciseFromWorkout(workoutExerciseId: Long)
    suspend fun reorderWorkoutExercises(workoutId: Long, exerciseIds: List<Long>)
    suspend fun addSetToWorkoutExercise(workoutExerciseId: Long, set: ExerciseSet): Long
    suspend fun updateSet(set: ExerciseSet)
    suspend fun deleteSet(setId: Long)
    suspend fun completeWorkout(workoutId: Long, duration: Int, caloriesBurned: Int?)
}

/**
 * Repository interface for Workout Routine operations
 */
interface WorkoutRoutineRepository {
    suspend fun createRoutine(routine: WorkoutRoutine): Long
    suspend fun updateRoutine(routine: WorkoutRoutine)
    suspend fun deleteRoutine(routineId: Long)
    fun getRoutineById(routineId: Long): Flow<WorkoutRoutine?>
    fun getRoutinesForUser(userId: Long): Flow<List<WorkoutRoutine>>
    suspend fun addExerciseToRoutine(routineId: Long, routineExercise: RoutineExercise): Long
    suspend fun updateRoutineExercise(routineExercise: RoutineExercise)
    suspend fun removeExerciseFromRoutine(routineExerciseId: Long)
    suspend fun reorderRoutineExercises(routineId: Long, exerciseIds: List<Long>)
    suspend fun createWorkoutFromRoutine(routineId: Long, userId: Long): Long
}

/**
 * Repository interface for Statistics operations
 */
interface StatisticsRepository {
    fun getWorkoutStatistics(userId: Long): Flow<WorkoutStatistics>
    fun getWeightProgressStats(userId: Long): Flow<List<Pair<LocalDate, Float>>>
    fun getWorkoutCountByMonth(userId: Long, year: Int): Flow<Map<Int, Int>>
    fun getExerciseProgressData(userId: Long, exerciseId: Long): Flow<List<Pair<LocalDate, Float>>>
}