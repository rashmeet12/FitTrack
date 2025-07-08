package com.example.fittrack.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

// file: SensorHelper.kt
object SensorHelper {
    fun hasStepCounter(context: Context): Boolean {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
    }
}
