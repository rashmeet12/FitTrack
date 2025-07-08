package com.example.fittrack.ui.step

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.CurrentUserPreference
import com.example.fittrack.repository.StepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class StepViewModel @Inject constructor(
    private val stepRepo: StepRepository,
    private val currentUserPref: CurrentUserPreference
) : ViewModel(), SensorEventListener {

    var stepsToday by mutableStateOf(0)
        private set

    var isTracking by mutableStateOf(false)
        private set

    var stepGoal by mutableStateOf(10000)
        private set

    var calories by mutableStateOf(0)
        private set

    var distance by mutableStateOf(0.0)
        private set

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var initialStepCount: Int? = null

    fun startTracking(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            isTracking = true
        }
    }

    fun stopTracking() {
        if (::sensorManager.isInitialized) {
            sensorManager.unregisterListener(this)
        }
        isTracking = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && isTracking) {
            val totalSteps = event.values[0].toInt()
            if (initialStepCount == null) {
                initialStepCount = totalSteps
            }
            stepsToday = totalSteps - (initialStepCount ?: 0)

            // Calculate calories (rough estimate: 0.04 calories per step)
            calories = (stepsToday * 0.04).toInt()

            // Calculate distance (rough estimate: 0.762 meters per step)
            distance = stepsToday * 0.000762 // in kilometers
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun saveTodaySteps() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUserPref.userIdFlow.collect { uid ->
                uid?.let {
                    val startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
                    stepRepo.addSteps(it, startOfDay, stepsToday)
                }
            }
        }
    }

    fun getProgressPercentage(): Float {
        return (stepsToday.toFloat() / stepGoal.toFloat()).coerceAtMost(1f)
    }

    override fun onCleared() {
        super.onCleared()
        if (isTracking) {
            stopTracking()
        }
    }
}