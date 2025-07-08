package com.example.fittrack.di
//
//import android.content.Context
//import androidx.room.Room
//import com.example.fittrack.data.local.dao.BmiDao
//import com.example.fittrack.data.local.dao.RouteDao
//import com.example.fittrack.data.local.dao.StepsDao
//import com.example.fittrack.data.local.database.AppDatabase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//// file: DatabaseModule.kt
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
//        Room.databaseBuilder(appContext, AppDatabase::class.java, "fitness_db").build()
//
//    @Provides
//    fun provideStepsDao(db: AppDatabase): StepsDao = db.stepsDao()
//
//    @Provides
//    fun provideBmiDao(db: AppDatabase): BmiDao = db.bmiDao()
//
//    @Provides
//    fun provideRouteDao(db: AppDatabase): RouteDao = db.routeDao()
//}
