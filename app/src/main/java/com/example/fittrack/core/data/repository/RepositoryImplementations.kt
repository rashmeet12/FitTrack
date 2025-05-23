package com.example.fittrack.core.data.repository

import com.example.fittrack.core.data.mapper.toDomainModel
import com.example.fittrack.core.data.mapper.toEntity
import com.example.fittrack.core.database.dao.ExerciseDao
import com.example.fittrack.core.database.dao.ExerciseSetDao
import com.example.fittrack.core.database.dao.RoutineExerciseDao
import com.example.fittrack.core.database.dao.UserDao
import com.example.fittrack.core.database.dao.WeightEntryDao
import com.example.fittrack.core.database.dao.WorkoutDao
import com.example.fittrack.core.database.dao.WorkoutExerciseDao
import com.example.fittrack.core.database.dao.WorkoutRoutineDao
import com.example.fittrack.core.database.entity.WorkoutEntity
import com.example.fittrack.core.database.relation.WorkoutExerciseWithDetails
import com.example.fittrack.core.database.relation.WorkoutWithExercises
import com.example.fittrack.core.domain.model.Exercise
import com.example.fittrack.core.domain.model.ExerciseSet
import com.example.fittrack.core.domain.model.RoutineExercise
import com.example.fittrack.core.domain.model.User
import com.example.fittrack.core.domain.model.WeightEntry
import com.example.fittrack.core.domain.model.Workout
import com.example.fittrack.core.domain.model.WorkoutExercise
import com.example.fittrack.core.domain.model.WorkoutRoutine
import com.example.fittrack.core.domain.model.WorkoutStatistics
import com.example.fittrack.core.domain.repository.ExerciseRepository
import com.example.fittrack.core.domain.repository.StatisticsRepository
import com.example.fittrack.core.domain.repository.UserRepository
import com.example.fittrack.core.domain.repository.WeightRepository
import com.example.fittrack.core.domain.repository.WorkoutRepository
import com.example.fittrack.core.domain.repository.WorkoutRoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun createUser(user: User): Long {
        return userDao.insertUser(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }

    override fun getUserById(userId: Long): Flow<User?> {
        return userDao.getUserById(userId).map { it?.toDomainModel() }
    }

    override fun getActiveUser(): Flow<User?> {
        return userDao.getActiveUser().map { it?.toDomainModel() }
    }

    override suspend fun deleteUser(userId: Long) {
        userDao.deleteUser(userId)
    }
}

@Singleton
class WeightRepositoryImpl @Inject constructor(
    private val weightEntryDao: WeightEntryDao
) : WeightRepository {

    override suspend fun addWeightEntry(entry: WeightEntry): Long {
        return weightEntryDao.insertWeightEntry(entry.toEntity())
    }

    override suspend fun updateWeightEntry(entry: WeightEntry) {
        weightEntryDao.updateWeightEntry(entry.toEntity())
    }

    override suspend fun deleteWeightEntry(entryId: Long) {
        weightEntryDao.getWeightEntryById(entryId).collect { entry ->
            entry?.let {
                weightEntryDao.deleteWeightEntry(it)
            }
        }
    }

    override fun getWeightEntriesForUser(userId: Long): Flow<List<WeightEntry>> {
        return weightEntryDao.getWeightEntriesForUser(userId).map { entries ->
            entries.map { it.toDomainModel() }
        }
    }

    override fun getWeightEntriesBetweenDates(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<WeightEntry>> {
        val startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

        return weightEntryDao.getWeightEntriesBetweenDates(userId, startMillis, endMillis).map { entries ->
            entries.map { it.toDomainModel() }
        }
    }

    override fun getLatestWeightEntry(userId: Long): Flow<WeightEntry?> {
        return weightEntryDao.getWeightEntriesForUser(userId).map { entries ->
            entries.firstOrNull()?.toDomainModel()
        }
    }
}

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {

    override suspend fun createExercise(exercise: Exercise): Long {
        return exerciseDao.insertExercise(exercise.toEntity())
    }

    override suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise.toEntity())
    }

    override suspend fun deleteExercise(exerciseId: Long) {
        exerciseDao.getExerciseById(exerciseId).collect { exercise ->
            exercise?.let {
                exerciseDao.deleteExercise(it)
            }
        }
    }

    override fun getExerciseById(exerciseId: Long): Flow<Exercise?> {
        return exerciseDao.getExerciseById(exerciseId).map { it?.toDomainModel() }
    }

    override fun getPresetExercises(): Flow<List<Exercise>> {
        return exerciseDao.getPresetExercises().map { exercises ->
            exercises.map { it.toDomainModel() }
        }
    }

    override fun getCustomExercisesForUser(userId: Long): Flow<List<Exercise>> {
        return exerciseDao.getCustomExercisesForUser(userId).map { exercises ->
            exercises.map { it.toDomainModel() }
        }
    }

    override fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<Exercise>> {
        return exerciseDao.getExercisesByMuscleGroup(muscleGroup).map { exercises ->
            exercises.map { it.toDomainModel() }
        }
    }

    override fun searchExercises(query: String): Flow<List<Exercise>> {
        return exerciseDao.searchExercises(query).map { exercises ->
            exercises.map { it.toDomainModel() }
        }
    }
}

@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val exerciseDao: ExerciseDao,
    private val exerciseSetDao: ExerciseSetDao
) : WorkoutRepository {

    override suspend fun createWorkout(workout: Workout): Long {
        // First insert the workout
        val workoutId = workoutDao.insertWorkout(workout.toEntity())

        // Then insert all exercises and sets
        workout.exercises.forEach { exercise ->
            val (workoutExerciseEntity, setEntities) = exercise.copy(workoutId = workoutId).toEntity()
            val workoutExerciseId = workoutExerciseDao.insertWorkoutExercise(workoutExerciseEntity)

            // Insert sets with the new workoutExerciseId
            setEntities.map { it.copy(workoutExerciseId = workoutExerciseId) }.let {
                if (it.isNotEmpty()) {
                    exerciseSetDao.insertAllSets(it)
                }
            }
        }

        return workoutId
    }

    override suspend fun updateWorkout(workout: Workout) {
        // Update the workout itself
        workoutDao.updateWorkout(workout.toEntity())

        // Handle updating exercises and sets - more complex logic needed here
        // Typically, you would fetch the existing workout exercises and sets
        // and perform appropriate updates, deletes, and inserts
    }

    override suspend fun deleteWorkout(workoutId: Long) {
        workoutDao.getWorkoutById(workoutId).collect { workout ->
            workout?.let {
                workoutDao.deleteWorkout(it)
            }
        }
    }

    override fun getWorkoutById(workoutId: Long): Flow<Workout?> {
        return workoutDao.getWorkoutById(workoutId).map { it?.toDomainModel() }
    }

    override fun getWorkoutsForUser(userId: Long): Flow<List<Workout>> {
        return workoutDao.getWorkoutsForUser(userId).map { workouts ->
            workouts.map { it.toDomainModel() }
        }
    }

    override fun getCompletedWorkoutsForUser(userId: Long): Flow<List<Workout>> {
        return workoutDao.getWorkoutsForUser(userId).map { workouts ->
            workouts.filter { it.isCompleted }.map { it.toDomainModel() }
        }
    }

    override fun getWorkoutsBetweenDates(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Workout>> {
        val startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

        return workoutDao.getWorkoutsBetweenDates(userId, startMillis, endMillis).map { workouts ->
            workouts.map { it.toDomainModel() }
        }
    }

    override fun getRecentCompletedWorkouts(userId: Long, limit: Int): Flow<List<Workout>> {
        return workoutDao.getRecentCompletedWorkouts(userId, limit).map { workouts ->
            workouts.map { it.toDomainModel() }
        }
    }

    override suspend fun addExerciseToWorkout(workoutId: Long, workoutExercise: WorkoutExercise): Long {
        val (workoutExerciseEntity, setEntities) = workoutExercise.toEntity()
        val workoutExerciseId = workoutExerciseDao.insertWorkoutExercise(workoutExerciseEntity)

        // Insert sets with the new workoutExerciseId
        setEntities.map { it.copy(workoutExerciseId = workoutExerciseId) }.let {
            if (it.isNotEmpty()) {
                exerciseSetDao.insertAllSets(it)
            }
        }

        return workoutExerciseId
    }

    override suspend fun updateWorkoutExercise(workoutExercise: WorkoutExercise) {
        val (workoutExerciseEntity, _) = workoutExercise.toEntity()
        workoutExerciseDao.updateWorkoutExercise(workoutExerciseEntity)
    }

    override suspend fun removeExerciseFromWorkout(workoutExerciseId: Long) {
        // First delete all sets for this exercise
        exerciseSetDao.deleteAllSetsForWorkoutExercise(workoutExerciseId)

        // Then delete the workout exercise
        workoutExerciseDao.getExercisesForWorkout(0).collect { exercises ->
            exercises.find { it.workoutExerciseId == workoutExerciseId }?.let {
                workoutExerciseDao.deleteWorkoutExercise(it)
            }
        }
    }

    override suspend fun reorderWorkoutExercises(workoutId: Long, exerciseIds: List<Long>) {
        // Get current exercises
        workoutExerciseDao.getExercisesForWorkout(workoutId).collect { exercises ->
            exercises.forEach { exercise ->
                val newIndex = exerciseIds.indexOf(exercise.workoutExerciseId)
                if (newIndex != -1) {
                    workoutExerciseDao.updateWorkoutExercise(
                        exercise.copy(orderIndex = newIndex)
                    )
                }
            }
        }
    }

    override suspend fun addSetToWorkoutExercise(workoutExerciseId: Long, set: ExerciseSet): Long {
        return exerciseSetDao.insertSet(set.toEntity())
    }

    override suspend fun updateSet(set: ExerciseSet) {
        exerciseSetDao.updateSet(set.toEntity())
    }

    override suspend fun deleteSet(setId: Long) {
        exerciseSetDao.getSetsForWorkoutExercise(0).collect { sets ->
            sets.find { it.setId == setId }?.let {
                exerciseSetDao.deleteSet(it)
            }
        }
    }

    override suspend fun completeWorkout(workoutId: Long, duration: Int, caloriesBurned: Int?) {
        workoutDao.getWorkoutById(workoutId).collect { workout ->
            workout?.let {
                val updatedWorkout = it.copy(
                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    isCompleted = true,
                    endTime = System.currentTimeMillis()
                )
                workoutDao.updateWorkout(updatedWorkout)
            }
        }
    }
}

@Singleton
class WorkoutRoutineRepositoryImpl @Inject constructor(
    private val workoutRoutineDao: WorkoutRoutineDao,
    private val routineExerciseDao: RoutineExerciseDao,
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao
) : WorkoutRoutineRepository {

    override suspend fun createRoutine(routine: WorkoutRoutine): Long {
        // First insert the routine
        val routineId = workoutRoutineDao.insertRoutine(routine.toEntity())

        // Then insert all exercises
        routine.exercises.forEach { exercise ->
            val routineExerciseEntity = exercise.copy(routineId = routineId).toEntity()
            routineExerciseDao.insertRoutineExercise(routineExerciseEntity)
        }

        return routineId
    }

    override suspend fun updateRoutine(routine: WorkoutRoutine) {
        workoutRoutineDao.updateRoutine(routine.toEntity())
    }

    override suspend fun deleteRoutine(routineId: Long) {
        workoutRoutineDao.getRoutineById(routineId).collect { routine ->
            routine?.let {
                workoutRoutineDao.deleteRoutine(it)
            }
        }
    }

    override fun getRoutineById(routineId: Long): Flow<WorkoutRoutine?> {
        return workoutRoutineDao.getRoutineById(routineId).map { it?.toDomainModel() }
    }

    override fun getRoutinesForUser(userId: Long): Flow<List<WorkoutRoutine>> {
        return workoutRoutineDao.getRoutinesForUser(userId).map { routines ->
            routines.map { it.toDomainModel() }
        }
    }

    override suspend fun addExerciseToRoutine(routineId: Long, routineExercise: RoutineExercise): Long {
        return routineExerciseDao.insertRoutineExercise(routineExercise.toEntity())
    }

    override suspend fun updateRoutineExercise(routineExercise: RoutineExercise) {
        routineExerciseDao.updateRoutineExercise(routineExercise.toEntity())
    }

    override suspend fun removeExerciseFromRoutine(routineExerciseId: Long) {
        routineExerciseDao.getExercisesForRoutine(0).collect { exercises ->
            exercises.find { it.routineExerciseId == routineExerciseId }?.let {
                routineExerciseDao.deleteRoutineExercise(it)
            }
        }
    }

    override suspend fun reorderRoutineExercises(routineId: Long, exerciseIds: List<Long>) {
        routineExerciseDao.getExercisesForRoutine(routineId).collect { exercises ->
            exercises.forEach { exercise ->
                val newIndex = exerciseIds.indexOf(exercise.routineExerciseId)
                if (newIndex != -1) {
                    routineExerciseDao.updateRoutineExercise(
                        exercise.copy(orderIndex = newIndex)
                    )
                }
            }
        }
    }

    override suspend fun createWorkoutFromRoutine(routineId: Long, userId: Long): Long {
        // This is a simplified implementation
        // In a real app, you'd want to create a proper workout with all exercises from the routine
        val currentTime = System.currentTimeMillis()
        val workout = WorkoutEntity(
            userId = userId,
            name = "Routine Workout",
            startTime = currentTime,
            date = currentTime,
            isCompleted = false
        )

        return workoutDao.insertWorkout(workout)
    }
}

@Singleton
class StatisticsRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val weightEntryDao: WeightEntryDao,
    private val exerciseSetDao: ExerciseSetDao
) : StatisticsRepository {

    override fun getWorkoutStatistics(userId: Long): Flow<WorkoutStatistics> {
        return workoutDao.getWorkoutsForUser(userId).map { workouts ->
            val completedWorkouts = workouts.filter { it.isCompleted }

            WorkoutStatistics(
                totalWorkouts = completedWorkouts.size,
                totalDuration = completedWorkouts.sumOf { it.duration },
                totalCaloriesBurned = completedWorkouts.sumOf { it.caloriesBurned ?: 0 },
                mostFrequentExercise = "Push-ups", // Placeholder - would need complex query
                strongestLift = Pair("Bench Press", 100f), // Placeholder - would need complex query
                workoutsThisWeek = 3, // Placeholder - would need date filtering
                workoutsLastWeek = 2, // Placeholder - would need date filtering
                averageWorkoutDuration = if (completedWorkouts.isNotEmpty()) {
                    completedWorkouts.sumOf { it.duration } / completedWorkouts.size
                } else 0
            )
        }
    }

    override fun getWeightProgressStats(userId: Long): Flow<List<Pair<LocalDate, Float>>> {
        return weightEntryDao.getWeightEntriesForUser(userId).map { entries ->
            entries.map { entry ->
                Pair(entry.toDomainModel().date, entry.weight)
            }
        }
    }

    override fun getWorkoutCountByMonth(userId: Long, year: Int): Flow<Map<Int, Int>> {
        return workoutDao.getWorkoutsForUser(userId).map { workouts ->
            // This would need proper date filtering in a real implementation
            mapOf(
                1 to 5, 2 to 7, 3 to 8, 4 to 6, 5 to 9, 6 to 11,
                7 to 10, 8 to 8, 9 to 7, 10 to 9, 11 to 6, 12 to 4
            )
        }
    }

    override fun getExerciseProgressData(userId: Long, exerciseId: Long): Flow<List<Pair<LocalDate, Float>>> {
        // This would need a complex query joining workouts, workout_exercises, and exercise_sets
        // For now, returning placeholder data
        return workoutDao.getWorkoutsForUser(userId).map {
            listOf(
                Pair(LocalDate.now().minusDays(30), 50f),
                Pair(LocalDate.now().minusDays(23), 55f),
                Pair(LocalDate.now().minusDays(16), 60f),
                Pair(LocalDate.now().minusDays(9), 62.5f),
                Pair(LocalDate.now().minusDays(2), 65f)
            )
        }
    }
}