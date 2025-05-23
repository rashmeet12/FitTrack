package com.example.fittrack.core.domain.usecase

import com.example.fittrack.core.domain.model.Exercise
import com.example.fittrack.core.domain.model.User
import com.example.fittrack.core.domain.model.WeightEntry
import com.example.fittrack.core.domain.model.Workout
import com.example.fittrack.core.domain.model.WorkoutRoutine
import com.example.fittrack.core.domain.model.WorkoutStatistics
import com.example.fittrack.core.domain.repository.ExerciseRepository
import com.example.fittrack.core.domain.repository.StatisticsRepository
import com.example.fittrack.core.domain.repository.UserRepository
import com.example.fittrack.core.domain.repository.WeightRepository
import com.example.fittrack.core.domain.repository.WorkoutRepository
import com.example.fittrack.core.domain.repository.WorkoutRoutineRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for user-related operations
 */
class UserUseCases @Inject constructor(
    private val userRepository: UserRepository
) {

    class CreateUser @Inject constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(user: User): Long {
            return userRepository.createUser(user)
        }
    }

    class GetActiveUser @Inject constructor(
        private val userRepository: UserRepository
    ) {
        operator fun invoke(): Flow<User?> {
            return userRepository.getActiveUser()
        }
    }

    class UpdateUser @Inject constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(user: User) {
            userRepository.updateUser(user)
        }
    }
}

/**
 * Use case for weight tracking operations
 */
class WeightUseCases @Inject constructor(
    private val weightRepository: WeightRepository
) {

    class AddWeightEntry @Inject constructor(
        private val weightRepository: WeightRepository
    ) {
        suspend operator fun invoke(entry: WeightEntry): Long {
            return weightRepository.addWeightEntry(entry)
        }
    }

    class GetWeightEntries @Inject constructor(
        private val weightRepository: WeightRepository
    ) {
        operator fun invoke(userId: Long): Flow<List<WeightEntry>> {
            return weightRepository.getWeightEntriesForUser(userId)
        }
    }

    class GetLatestWeight @Inject constructor(
        private val weightRepository: WeightRepository
    ) {
        operator fun invoke(userId: Long): Flow<WeightEntry?> {
            return weightRepository.getLatestWeightEntry(userId)
        }
    }
}

/**
 * Use case for exercise-related operations
 */
class ExerciseUseCases @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {

    class GetPresetExercises @Inject constructor(
        private val exerciseRepository: ExerciseRepository
    ) {
        operator fun invoke(): Flow<List<Exercise>> {
            return exerciseRepository.getPresetExercises()
        }
    }

    class GetCustomExercises @Inject constructor(
        private val exerciseRepository: ExerciseRepository
    ) {
        operator fun invoke(userId: Long): Flow<List<Exercise>> {
            return exerciseRepository.getCustomExercisesForUser(userId)
        }
    }

    class SearchExercises @Inject constructor(
        private val exerciseRepository: ExerciseRepository
    ) {
        operator fun invoke(query: String): Flow<List<Exercise>> {
            return exerciseRepository.searchExercises(query)
        }
    }

    class CreateCustomExercise @Inject constructor(
        private val exerciseRepository: ExerciseRepository
    ) {
        suspend operator fun invoke(exercise: Exercise): Long {
            return exerciseRepository.createExercise(exercise)
        }
    }

    class GetExercisesByMuscleGroup @Inject constructor(
        private val exerciseRepository: ExerciseRepository
    ) {
        operator fun invoke(muscleGroup: String): Flow<List<Exercise>> {
            return exerciseRepository.getExercisesByMuscleGroup(muscleGroup)
        }
    }
}

/**
 * Use case for workout-related operations
 */
class WorkoutUseCases @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {

    class CreateWorkout @Inject constructor(
        private val workoutRepository: WorkoutRepository
    ) {
        suspend operator fun invoke(workout: Workout): Long {
            return workoutRepository.createWorkout(workout)
        }
    }

    class GetWorkoutsForUser @Inject constructor(
        private val workoutRepository: WorkoutRepository
    ) {
        operator fun invoke(userId: Long): Flow<List<Workout>> {
            return workoutRepository.getWorkoutsForUser(userId)
        }
    }

    class GetWorkoutById @Inject constructor(
        private val workoutRepository: WorkoutRepository
    ) {
        operator fun invoke(workoutId: Long): Flow<Workout?> {
            return workoutRepository.getWorkoutById(workoutId)
        }
    }

    class CompleteWorkout @Inject constructor(
        private val workoutRepository: WorkoutRepository
    ) {
        suspend operator fun invoke(workoutId: Long, duration: Int, caloriesBurned: Int?) {
            workoutRepository.completeWorkout(workoutId, duration, caloriesBurned)
        }
    }

    class GetRecentWorkouts @Inject constructor(
        private val workoutRepository: WorkoutRepository
    ) {
        operator fun invoke(userId: Long, limit: Int = 5): Flow<List<Workout>> {
            return workoutRepository.getRecentCompletedWorkouts(userId, limit)
        }
    }

    class GetWorkoutsBetweenDates @Inject constructor(
        private val workoutRepository: WorkoutRepository
    ) {
        operator fun invoke(userId: Long, startDate: LocalDate, endDate: LocalDate): Flow<List<Workout>> {
            return workoutRepository.getWorkoutsBetweenDates(userId, startDate, endDate)
        }
    }
}

/**
 * Use case for workout routine operations
 */
class WorkoutRoutineUseCases @Inject constructor(
    private val workoutRoutineRepository: WorkoutRoutineRepository
) {

    class CreateRoutine @Inject constructor(
        private val workoutRoutineRepository: WorkoutRoutineRepository
    ) {
        suspend operator fun invoke(routine: WorkoutRoutine): Long {
            return workoutRoutineRepository.createRoutine(routine)
        }
    }

    class GetRoutinesForUser @Inject constructor(
        private val workoutRoutineRepository: WorkoutRoutineRepository
    ) {
        operator fun invoke(userId: Long): Flow<List<WorkoutRoutine>> {
            return workoutRoutineRepository.getRoutinesForUser(userId)
        }
    }

    class GetRoutineById @Inject constructor(
        private val workoutRoutineRepository: WorkoutRoutineRepository
    ) {
        operator fun invoke(routineId: Long): Flow<WorkoutRoutine?> {
            return workoutRoutineRepository.getRoutineById(routineId)
        }
    }

    class CreateWorkoutFromRoutine @Inject constructor(
        private val workoutRoutineRepository: WorkoutRoutineRepository
    ) {
        suspend operator fun invoke(routineId: Long, userId: Long): Long {
            return workoutRoutineRepository.createWorkoutFromRoutine(routineId, userId)
        }
    }
}

/**
 * Use case for statistics operations
 */
class StatisticsUseCases @Inject constructor(
    private val statisticsRepository: StatisticsRepository
) {

    class GetWorkoutStatistics @Inject constructor(
        private val statisticsRepository: StatisticsRepository
    ) {
        operator fun invoke(userId: Long): Flow<WorkoutStatistics> {
            return statisticsRepository.getWorkoutStatistics(userId)
        }
    }

    class GetWeightProgress @Inject constructor(
        private val statisticsRepository: StatisticsRepository
    ) {
        operator fun invoke(userId: Long): Flow<List<Pair<LocalDate, Float>>> {
            return statisticsRepository.getWeightProgressStats(userId)
        }
    }

    class GetMonthlyWorkoutCount @Inject constructor(
        private val statisticsRepository: StatisticsRepository
    ) {
        operator fun invoke(userId: Long, year: Int): Flow<Map<Int, Int>> {
            return statisticsRepository.getWorkoutCountByMonth(userId, year)
        }
    }

    class GetExerciseProgress @Inject constructor(
        private val statisticsRepository: StatisticsRepository
    ) {
        operator fun invoke(userId: Long, exerciseId: Long): Flow<List<Pair<LocalDate, Float>>> {
            return statisticsRepository.getExerciseProgressData(userId, exerciseId)
        }
    }
}