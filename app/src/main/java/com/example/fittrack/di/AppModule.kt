package com.example.fittrack.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.fittrack.CurrentUserPreference
import com.example.fittrack.data.FitnessDatabase
import com.example.fittrack.data.dao.ActivityDao
import com.example.fittrack.data.dao.BmiDao
import com.example.fittrack.data.dao.RouteDao
import com.example.fittrack.data.dao.StepDao
import com.example.fittrack.data.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// file: AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FitnessDatabase {
        return Room.databaseBuilder(appContext, FitnessDatabase::class.java, "fitness_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideUserDao(db: FitnessDatabase): UserDao = db.userDao()
    @Provides fun provideStepDao(db: FitnessDatabase): StepDao = db.stepDao()
    @Provides fun provideBmiDao(db: FitnessDatabase): BmiDao = db.bmiDao()
    @Provides fun provideRouteDao(db: FitnessDatabase): RouteDao = db.routeDao()
    @Provides fun provideActivityDao(db: FitnessDatabase): ActivityDao = db.activityDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("user_prefs")
        }
    }

    @Provides
    @Singleton
    fun provideCurrentUserPreference(dataStore: DataStore<Preferences>): CurrentUserPreference {
        return CurrentUserPreference(dataStore)
    }


}
