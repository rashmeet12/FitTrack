package com.example.fittrack

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CurrentUserPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val useridkey = stringPreferencesKey("current_user_id")

    suspend fun saveUserId(userId: String) {
        dataStore.edit { prefs ->
            prefs[useridkey] = userId
        }
    }

    val userIdFlow: Flow<String?> = dataStore.data
        .map {  prefs ->
            prefs[longPreferencesKey("current_user_id")]?.toString()
                ?: prefs[stringPreferencesKey("current_user_id")] }
}

