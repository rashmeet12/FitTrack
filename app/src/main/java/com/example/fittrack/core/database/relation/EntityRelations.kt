package com.example.fittrack.core.database.relation


import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.fittrack.core.database.entity.ExerciseEntity
import com.example.fittrack.core.database.entity.ExerciseSetEntity
import com.example.fittrack.core.database.entity.RoutineExerciseEntity
import com.example.fittrack.core.database.entity.WorkoutEntity
import com.example.fittrack.core.database.entity.WorkoutExerciseEntity
import com.example.fittrack.core.database.entity.WorkoutRoutineEntity

/**
 * Represents a complete workout with all its exercises and sets
 */
data class WorkoutWithExercises(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        entity = WorkoutExerciseEntity::class,
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val workoutExercises: List<WorkoutExerciseWithDetails>
)

/**
 * Represents a workout exercise with the exercise details and all sets
 */
data class WorkoutExerciseWithDetails(
    @Embedded val workoutExercise: WorkoutExerciseEntity,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exercise: ExerciseEntity,
    @Relation(
        parentColumn = "workoutExerciseId",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<ExerciseSetEntity>
)

/**
 * Represents a workout routine with all its exercises
 */
data class WorkoutRoutineWithExercises(
    @Embedded val routine: WorkoutRoutineEntity,
    @Relation(
        entity = RoutineExerciseEntity::class,
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val routineExercises: List<RoutineExerciseWithDetails>
)

/**
 * Represents a routine exercise with the exercise details
 */
data class RoutineExerciseWithDetails(
    @Embedded val routineExercise: RoutineExerciseEntity,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exercise: ExerciseEntity
)