package com.example.fittrack.ui.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.CurrentUserPreference
import com.example.fittrack.data.model.ActivityEntity
import com.example.fittrack.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val activityRepo: ActivityRepository,
    private val currentUserPref: CurrentUserPreference
) : ViewModel() {
    var isRunning by mutableStateOf(false)
        private set
    var activityName by mutableStateOf("")
        private set
    var startTime: Long = 0
        private set

    // Store activities list
    val activities = mutableStateListOf<ActivityEntity>()

    // Temporary storage for current activity before save/discard decision
    private var currentEndTime: Long = 0
    private var tempActivitySaved = false

    init {
        loadActivities()
    }

    fun startActivity(name: String) {
        activityName = name
        isRunning = true
        startTime = System.currentTimeMillis()
        tempActivitySaved = false
    }

    fun stopActivity() {
        if (isRunning) {
            currentEndTime = System.currentTimeMillis()
            isRunning = false
            // Don't automatically save - let user decide in dialog
        }
    }

    fun saveCurrentActivity() {
        if (!tempActivitySaved && activityName.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                currentUserPref.userIdFlow.firstOrNull()?.let { uid ->
                    activityRepo.addActivity(uid, activityName, startTime, currentEndTime)
                    loadActivities() // Refresh the list
                }
            }
            resetCurrentActivity()
        }
    }

    fun discardCurrentActivity() {
        resetCurrentActivity()
    }

    private fun resetCurrentActivity() {
        activityName = ""
        startTime = 0
        currentEndTime = 0
        tempActivitySaved = true
    }

    fun deleteActivity(activityId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            activityRepo.deleteActivity(activityId)
            loadActivities() // Refresh the list
        }
    }

    private fun loadActivities() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUserPref.userIdFlow.collect { uid ->
                uid?.let {
                    val activityList = activityRepo.getActivities(it)
                    withContext(Dispatchers.Main) {
                        activities.clear()
                        activities.addAll(activityList.sortedByDescending { activity -> activity.startTime })
                    }
                }
            }
        }
    }
}