package com.example.fittrack.core.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.fittrack.core.data.repository.ExerciseRepositoryImpl
import com.example.fittrack.core.data.repository.StatisticsRepositoryImpl
import com.example.fittrack.core.data.repository.UserRepositoryImpl
import com.example.fittrack.core.data.repository.WeightRepositoryImpl
import com.example.fittrack.core.data.repository.WorkoutRepositoryImpl
import com.example.fittrack.core.data.repository.WorkoutRoutineRepositoryImpl
import com.example.fittrack.core.database.FitTrackDatabase
import com.example.fittrack.core.database.dao.ExerciseDao
import com.example.fittrack.core.database.dao.ExerciseSetDao
import com.example.fittrack.core.database.dao.RoutineExerciseDao
import com.example.fittrack.core.database.dao.UserDao
import com.example.fittrack.core.database.dao.WeightEntryDao
import com.example.fittrack.core.database.dao.WorkoutDao
import com.example.fittrack.core.database.dao.WorkoutExerciseDao
import com.example.fittrack.core.database.dao.WorkoutRoutineDao
import com.example.fittrack.core.datastore.UserPreferencesRepository
import com.example.fittrack.core.datastore.userPreferencesDataStore
import com.example.fittrack.core.domain.repository.ExerciseRepository
import com.example.fittrack.core.domain.repository.StatisticsRepository
import com.example.fittrack.core.domain.repository.UserRepository
import com.example.fittrack.core.domain.repository.WeightRepository
import com.example.fittrack.core.domain.repository.WorkoutRepository
import com.example.fittrack.core.domain.repository.WorkoutRoutineRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FitTrackDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FitTrackDatabase::class.java,
            "fittrack_database"
        )
            .fallbackToDestructiveMigration() // Only for development - remove in production
            .build()
    }

    @Provides
    fun provideUserDao(database: FitTrackDatabase): UserDao = database.userDao()

    @Provides
    fun provideWeightEntryDao(database: FitTrackDatabase): WeightEntryDao = database.weightEntryDao()

    @Provides
    fun provideExerciseDao(database: FitTrackDatabase): ExerciseDao = database.exerciseDao()

    @Provides
    fun provideWorkoutDao(database: FitTrackDatabase): WorkoutDao = database.workoutDao()

    @Provides
    fun provideWorkoutExerciseDao(database: FitTrackDatabase): WorkoutExerciseDao = database.workoutExerciseDao()

    @Provides
    fun provideExerciseSetDao(database: FitTrackDatabase): ExerciseSetDao = database.exerciseSetDao()

    @Provides
    fun provideWorkoutRoutineDao(database: FitTrackDatabase): WorkoutRoutineDao = database.workoutRoutineDao()

    @Provides
    fun provideRoutineExerciseDao(database: FitTrackDatabase): RoutineExerciseDao = database.routineExerciseDao()
}

/**
 * Hilt module for providing DataStore dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.userPreferencesDataStore
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }
}

/**
 * Hilt module for binding repository implementations to interfaces
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindWeightRepository(weightRepositoryImpl: WeightRepositoryImpl): WeightRepository

    @Binds
    abstract fun bindExerciseRepository(exerciseRepositoryImpl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    abstract fun bindWorkoutRepository(workoutRepositoryImpl: WorkoutRepositoryImpl): WorkoutRepository

    @Binds
    abstract fun bindWorkoutRoutineRepository(
        workoutRoutineRepositoryImpl: WorkoutRoutineRepositoryImpl
    ): WorkoutRoutineRepository

    @Binds
    abstract fun bindStatisticsRepository(statisticsRepositoryImpl: StatisticsRepositoryImpl): StatisticsRepository
}