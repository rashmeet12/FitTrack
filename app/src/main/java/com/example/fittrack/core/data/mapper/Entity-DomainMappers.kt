package com.example.fittrack.core.data.mapper


import com.example.fittrack.core.database.entity.ExerciseEntity
import com.example.fittrack.core.database.entity.ExerciseSetEntity
import com.example.fittrack.core.database.entity.RoutineExerciseEntity
import com.example.fittrack.core.database.entity.UserEntity
import com.example.fittrack.core.database.entity.WeightEntryEntity
import com.example.fittrack.core.database.entity.WorkoutEntity
import com.example.fittrack.core.database.entity.WorkoutExerciseEntity
import com.example.fittrack.core.database.entity.WorkoutRoutineEntity
import com.example.fittrack.core.database.relation.RoutineExerciseWithDetails
import com.example.fittrack.core.database.relation.WorkoutExerciseWithDetails
import com.example.fittrack.core.database.relation.WorkoutRoutineWithExercises
import com.example.fittrack.core.database.relation.WorkoutWithExercises
import com.example.fittrack.core.domain.model.Exercise
import com.example.fittrack.core.domain.model.ExerciseSet
import com.example.fittrack.core.domain.model.RoutineExercise
import com.example.fittrack.core.domain.model.User
import com.example.fittrack.core.domain.model.WeightEntry
import com.example.fittrack.core.domain.model.Workout
import com.example.fittrack.core.domain.model.WorkoutExercise
import com.example.fittrack.core.domain.model.WorkoutRoutine
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Mapper functions to convert between entity and domain models
 */

// User mapper functions
fun UserEntity.toDomainModel(): User {
    return User(
        id = userId,
        name = name,
        age = age,
        height = height,
        weight = weight,
        gender = gender,
        fitnessGoal = fitnessGoal,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = id,
        name = name,
        age = age,
        height = height,
        weight = weight,
        gender = gender,
        fitnessGoal = fitnessGoal,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// WeightEntry mapper functions
fun WeightEntryEntity.toDomainModel(): WeightEntry {
    return WeightEntry(
        id = entryId,
        userId = userId,
        weight = weight,
        date = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate(),
        note = note
    )
}

fun WeightEntry.toEntity(): WeightEntryEntity {
    return WeightEntryEntity(
        entryId = id,
        userId = userId,
        weight = weight,
        date = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        note = note
    )
}

// Exercise mapper functions
fun ExerciseEntity.toDomainModel(): Exercise {
    return Exercise(
        id = exerciseId,
        name = name,
        description = description,
        targetMuscleGroup = targetMuscleGroup,
        isCustom = isCustom,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
        createdBy = createdBy,
        createdAt = createdAt
    )
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(
        exerciseId = id,
        name = name,
        description = description,
        targetMuscleGroup = targetMuscleGroup,
        isCustom = isCustom,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
        createdBy = createdBy,
        createdAt = createdAt
    )
}

// Workout mapper functions
fun WorkoutEntity.toDomainModel(): Workout {
    return Workout(
        id = workoutId,
        userId = userId,
        name = name,
        description = description,
        duration = duration,
        caloriesBurned = caloriesBurned,
        date = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate(),
        startTime = startTime,
        endTime = endTime,
        isCompleted = isCompleted
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        workoutId = id,
        userId = userId,
        name = name,
        description = description,
        duration = duration,
        caloriesBurned = caloriesBurned,
        date = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        startTime = startTime,
        endTime = endTime,
        isCompleted = isCompleted
    )
}

// Exercise Set mapper functions
fun ExerciseSetEntity.toDomainModel(): ExerciseSet {
    return ExerciseSet(
        id = setId,
        workoutExerciseId = workoutExerciseId,
        setNumber = setNumber,
        reps = reps,
        weight = weight,
        duration = duration,
        completed = completed,
        timestamp = timestamp
    )
}

fun ExerciseSet.toEntity(): ExerciseSetEntity {
    return ExerciseSetEntity(
        setId = id,
        workoutExerciseId = workoutExerciseId,
        setNumber = setNumber,
        reps = reps,
        weight = weight,
        duration = duration,
        completed = completed,
        timestamp = timestamp
    )
}

// Complex mappers for relationships
fun WorkoutExerciseWithDetails.toDomainModel(): WorkoutExercise {
    return WorkoutExercise(
        id = workoutExercise.workoutExerciseId,
        workoutId = workoutExercise.workoutId,
        exercise = exercise.toDomainModel(),
        orderIndex = workoutExercise.orderIndex,
        targetSets = workoutExercise.targetSets,
        targetReps = workoutExercise.targetReps,
        targetDuration = workoutExercise.targetDuration,
        restBetweenSets = workoutExercise.restBetweenSets,
        sets = sets.map { it.toDomainModel() }
    )
}

fun WorkoutExercise.toEntity(): Pair<WorkoutExerciseEntity, List<ExerciseSetEntity>> {
    val workoutExerciseEntity = WorkoutExerciseEntity(
        workoutExerciseId = id,
        workoutId = workoutId,
        exerciseId = exercise.id,
        orderIndex = orderIndex,
        targetSets = targetSets,
        targetReps = targetReps,
        targetDuration = targetDuration,
        restBetweenSets = restBetweenSets
    )

    val setEntities = sets.map { it.toEntity() }

    return Pair(workoutExerciseEntity, setEntities)
}

fun WorkoutWithExercises.toDomainModel(): Workout {
    return Workout(
        id = workout.workoutId,
        userId = workout.userId,
        name = workout.name,
        description = workout.description,
        duration = workout.duration,
        caloriesBurned = workout.caloriesBurned,
        date = Instant.ofEpochMilli(workout.date).atZone(ZoneId.systemDefault()).toLocalDate(),
        startTime = workout.startTime,
        endTime = workout.endTime,
        isCompleted = workout.isCompleted,
        exercises = workoutExercises.map { it.toDomainModel() }
    )
}

// Routine mapper functions
fun WorkoutRoutineEntity.toDomainModel(): WorkoutRoutine {
    return WorkoutRoutine(
        id = routineId,
        userId = userId,
        name = name,
        description = description,
        frequency = frequency,
        createdAt = createdAt
    )
}

fun WorkoutRoutine.toEntity(): WorkoutRoutineEntity {
    return WorkoutRoutineEntity(
        routineId = id,
        userId = userId,
        name = name,
        description = description,
        frequency = frequency,
        createdAt = createdAt
    )
}

// Routine Exercise mapper functions
fun RoutineExerciseWithDetails.toDomainModel(): RoutineExercise {
    return RoutineExercise(
        id = routineExercise.routineExerciseId,
        routineId = routineExercise.routineId,
        exercise = exercise.toDomainModel(),
        orderIndex = routineExercise.orderIndex,
        targetSets = routineExercise.targetSets,
        targetReps = routineExercise.targetReps,
        targetDuration = routineExercise.targetDuration,
        restBetweenSets = routineExercise.restBetweenSets
    )
}

fun RoutineExercise.toEntity(): RoutineExerciseEntity {
    return RoutineExerciseEntity(
        routineExerciseId = id,
        routineId = routineId,
        exerciseId = exercise.id,
        orderIndex = orderIndex,
        targetSets = targetSets,
        targetReps = targetReps,
        targetDuration = targetDuration,
        restBetweenSets = restBetweenSets
    )
}

fun WorkoutRoutineWithExercises.toDomainModel(): WorkoutRoutine {
    return WorkoutRoutine(
        id = routine.routineId,
        userId = routine.userId,
        name = routine.name,
        description = routine.description,
        frequency = routine.frequency,
        createdAt = routine.createdAt,
        exercises = routineExercises.map { it.toDomainModel() }
    )
}