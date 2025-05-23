package com.example.fittrack.core.database.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fittrack.core.database.entity.ExerciseEntity
import com.example.fittrack.core.database.entity.ExerciseSetEntity
import com.example.fittrack.core.database.entity.RoutineExerciseEntity
import com.example.fittrack.core.database.entity.UserEntity
import com.example.fittrack.core.database.entity.WeightEntryEntity
import com.example.fittrack.core.database.entity.WorkoutEntity
import com.example.fittrack.core.database.entity.WorkoutExerciseEntity
import com.example.fittrack.core.database.entity.WorkoutRoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    fun getActiveUser(): Flow<UserEntity?>

    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteUser(userId: Long)
}

@Dao
interface WeightEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightEntry(weightEntry: WeightEntryEntity): Long

    @Update
    suspend fun updateWeightEntry(weightEntry: WeightEntryEntity)

    @Delete
    suspend fun deleteWeightEntry(weightEntry: WeightEntryEntity)

    @Query("SELECT * FROM weight_entries WHERE userId = :userId ORDER BY date DESC")
    fun getWeightEntriesForUser(userId: Long): Flow<List<WeightEntryEntity>>

    @Query("SELECT * FROM weight_entries WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getWeightEntriesBetweenDates(userId: Long, startDate: Long, endDate: Long): Flow<List<WeightEntryEntity>>

    @Query("SELECT * FROM weight_entries WHERE entryId = :entryId")
    fun getWeightEntryById(entryId: Long): Flow<WeightEntryEntity?>
}

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId")
    fun getExerciseById(exerciseId: Long): Flow<ExerciseEntity?>

    @Query("SELECT * FROM exercises WHERE isCustom = 0 ORDER BY name ASC")
    fun getPresetExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE isCustom = 1 AND createdBy = :userId ORDER BY name ASC")
    fun getCustomExercisesForUser(userId: Long): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE targetMuscleGroup = :muscleGroup ORDER BY name ASC")
    fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchExercises(query: String): Flow<List<ExerciseEntity>>
}

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    fun getWorkoutById(workoutId: Long): Flow<WorkoutEntity?>

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY date DESC")
    fun getWorkoutsForUser(userId: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getWorkoutsBetweenDates(userId: Long, startDate: Long, endDate: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND isCompleted = 1 ORDER BY date DESC LIMIT :limit")
    fun getRecentCompletedWorkouts(userId: Long, limit: Int): Flow<List<WorkoutEntity>>
}

@Dao
interface WorkoutExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWorkoutExercises(workoutExercises: List<WorkoutExerciseEntity>): List<Long>

    @Update
    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExerciseEntity)

    @Delete
    suspend fun deleteWorkoutExercise(workoutExercise: WorkoutExerciseEntity)

    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY orderIndex ASC")
    fun getExercisesForWorkout(workoutId: Long): Flow<List<WorkoutExerciseEntity>>

    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId")
    suspend fun deleteAllExercisesForWorkout(workoutId: Long)
}

@Dao
interface ExerciseSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: ExerciseSetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSets(sets: List<ExerciseSetEntity>): List<Long>

    @Update
    suspend fun updateSet(set: ExerciseSetEntity)

    @Delete
    suspend fun deleteSet(set: ExerciseSetEntity)

    @Query("SELECT * FROM exercise_sets WHERE workoutExerciseId = :workoutExerciseId ORDER BY setNumber ASC")
    fun getSetsForWorkoutExercise(workoutExerciseId: Long): Flow<List<ExerciseSetEntity>>

    @Query("DELETE FROM exercise_sets WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun deleteAllSetsForWorkoutExercise(workoutExerciseId: Long)
}

@Dao
interface WorkoutRoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: WorkoutRoutineEntity): Long

    @Update
    suspend fun updateRoutine(routine: WorkoutRoutineEntity)

    @Delete
    suspend fun deleteRoutine(routine: WorkoutRoutineEntity)

    @Query("SELECT * FROM workout_routines WHERE routineId = :routineId")
    fun getRoutineById(routineId: Long): Flow<WorkoutRoutineEntity?>

    @Query("SELECT * FROM workout_routines WHERE userId = :userId ORDER BY name ASC")
    fun getRoutinesForUser(userId: Long): Flow<List<WorkoutRoutineEntity>>
}

@Dao
interface RoutineExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExercise(routineExercise: RoutineExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoutineExercises(routineExercises: List<RoutineExerciseEntity>): List<Long>

    @Update
    suspend fun updateRoutineExercise(routineExercise: RoutineExerciseEntity)

    @Delete
    suspend fun deleteRoutineExercise(routineExercise: RoutineExerciseEntity)

    @Query("SELECT * FROM routine_exercises WHERE routineId = :routineId ORDER BY orderIndex ASC")
    fun getExercisesForRoutine(routineId: Long): Flow<List<RoutineExerciseEntity>>

    @Query("DELETE FROM routine_exercises WHERE routineId = :routineId")
    suspend fun deleteAllExercisesForRoutine(routineId: Long)
}