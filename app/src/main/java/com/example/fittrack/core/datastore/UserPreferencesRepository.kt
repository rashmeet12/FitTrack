package com.example.fittrack.core.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extension property to get DataStore instance
val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

/**
 * Repository for managing user preferences using DataStore
 */
@Singleton
class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDataStore: DataStore<Preferences>
) {

    // Preference keys
    object PreferencesKeys {
        val ACTIVE_USER_ID = longPreferencesKey("active_user_id")
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit") // "kg" or "lb"
        val HEIGHT_UNIT = stringPreferencesKey("height_unit") // "cm" or "in"
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val REST_TIMER_DURATION = intPreferencesKey("rest_timer_duration") // In seconds
        val REST_TIMER_ENABLED = booleanPreferencesKey("rest_timer_enabled")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val WORKOUT_REMINDER_TIME = longPreferencesKey("workout_reminder_time") // In milliseconds
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
    }

    /**
     * Get the active user ID
     */
    val activeUserId: Flow<Long?> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACTIVE_USER_ID]
    }

    /**
     * Set the active user ID
     */
    suspend fun setActiveUserId(userId: Long) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_USER_ID] = userId
        }
    }

    /**
     * Get the weight unit (kg or lb)
     */
    val weightUnit: Flow<String> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.WEIGHT_UNIT] ?: "kg" // Default to kg
    }

    /**
     * Set the weight unit
     */
    suspend fun setWeightUnit(unit: String) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.WEIGHT_UNIT] = unit
        }
    }

    /**
     * Get the height unit (cm or in)
     */
    val heightUnit: Flow<String> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.HEIGHT_UNIT] ?: "cm" // Default to cm
    }

    /**
     * Set the height unit
     */
    suspend fun setHeightUnit(unit: String) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.HEIGHT_UNIT] = unit
        }
    }

    /**
     * Get the theme preference
     */
    val isDarkTheme: Flow<Boolean> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_DARK_THEME] ?: false // Default to light theme
    }

    /**
     * Set the theme preference
     */
    suspend fun setDarkTheme(isDark: Boolean) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDark
        }
    }

    /**
     * Get the rest timer duration
     */
    val restTimerDuration: Flow<Int> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.REST_TIMER_DURATION] ?: 60 // Default to 60 seconds
    }

    /**
     * Set the rest timer duration
     */
    suspend fun setRestTimerDuration(duration: Int) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.REST_TIMER_DURATION] = duration
        }
    }

    /**
     * Get whether the rest timer is enabled
     */
    val isRestTimerEnabled: Flow<Boolean> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.REST_TIMER_ENABLED] ?: true // Default to enabled
    }

    /**
     * Set whether the rest timer is enabled
     */
    suspend fun setRestTimerEnabled(enabled: Boolean) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.REST_TIMER_ENABLED] = enabled
        }
    }

    /**
     * Get whether notifications are enabled
     */
    val isNotificationEnabled: Flow<Boolean> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATION_ENABLED] ?: true // Default to enabled
    }

    /**
     * Set whether notifications are enabled
     */
    suspend fun setNotificationEnabled(enabled: Boolean) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_ENABLED] = enabled
        }
    }

    /**
     * Get the workout reminder time
     */
    val workoutReminderTime: Flow<Long?> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.WORKOUT_REMINDER_TIME]
    }

    /**
     * Set the workout reminder time
     */
    suspend fun setWorkoutReminderTime(time: Long) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.WORKOUT_REMINDER_TIME] = time
        }
    }

    /**
     * Get whether the user has completed onboarding
     */
    val hasCompletedOnboarding: Flow<Boolean> = userPreferencesDataStore.data.map { preferences ->
        preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false
    }

    /**
     * Set whether the user has completed onboarding
     */
    suspend fun setHasCompletedOnboarding(completed: Boolean) {
        userPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }
}