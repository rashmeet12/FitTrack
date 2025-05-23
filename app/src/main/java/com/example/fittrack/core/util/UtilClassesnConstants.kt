package com.example.fittrack.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

/**
 * Constants used throughout the application
 */
object Constants {
    // Database constants
    const val DATABASE_NAME = "fittrack_database"
    const val DATABASE_VERSION = 1

    // Preferences constants
    const val WEIGHT_UNIT_KG = "kg"
    const val WEIGHT_UNIT_LB = "lb"
    const val HEIGHT_UNIT_CM = "cm"
    const val HEIGHT_UNIT_IN = "in"

    // Default values
    const val DEFAULT_REST_TIMER_SECONDS = 60
    const val DEFAULT_WORKOUT_DURATION_MINUTES = 60
    const val MAX_WORKOUT_NAME_LENGTH = 50
    const val MAX_EXERCISE_NAME_LENGTH = 100
    const val MAX_EXERCISE_DESCRIPTION_LENGTH = 500

    // Muscle groups
    val MUSCLE_GROUPS = listOf(
        "Chest", "Back", "Shoulders", "Arms", "Legs", "Core", "Cardio"
    )

    // Fitness goals
    val FITNESS_GOALS = listOf(
        "Lose Weight", "Gain Muscle", "Improve Endurance", "General Fitness", "Strength Training"
    )

    // Gender options
    val GENDER_OPTIONS = listOf("Male", "Female", "Other")
}

/**
 * Utility functions for date operations
 */
object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    private val shortDateFormatter = DateTimeFormatter.ofPattern("MM/dd")

    fun formatDate(date: LocalDate): String {
        return date.format(dateFormatter)
    }

    fun formatShortDate(date: LocalDate): String {
        return date.format(shortDateFormatter)
    }

    fun getStartOfWeek(date: LocalDate = LocalDate.now()): LocalDate {
        return date.minusDays(date.dayOfWeek.value.toLong() - 1)
    }

    fun getEndOfWeek(date: LocalDate = LocalDate.now()): LocalDate {
        return getStartOfWeek(date).plusDays(6)
    }

    fun getDaysBetween(startDate: LocalDate, endDate: LocalDate): Long {
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    fun isToday(date: LocalDate): Boolean {
        return date == LocalDate.now()
    }

    fun isThisWeek(date: LocalDate): Boolean {
        val today = LocalDate.now()
        val startOfWeek = getStartOfWeek(today)
        val endOfWeek = getEndOfWeek(today)
        return date.isAfter(startOfWeek.minusDays(1)) && date.isBefore(endOfWeek.plusDays(1))
    }
}

/**
 * Utility functions for unit conversions
 */
object UnitUtils {
    fun kgToLb(kg: Float): Float {
        return kg * 2.20462f
    }

    fun lbToKg(lb: Float): Float {
        return lb / 2.20462f
    }

    fun cmToIn(cm: Float): Float {
        return cm / 2.54f
    }

    fun inToCm(inches: Float): Float {
        return inches * 2.54f
    }

    fun formatWeight(weight: Float, unit: String): String {
        return "${weight.roundToInt()} $unit"
    }

    fun formatHeight(height: Float, unit: String): String {
        return if (unit == Constants.HEIGHT_UNIT_CM) {
            "${height.roundToInt()} cm"
        } else {
            val totalInches = cmToIn(height)
            val feet = (totalInches / 12).toInt()
            val inches = (totalInches % 12).roundToInt()
            "${feet}'${inches}\""
        }
    }
}

/**
 * Utility functions for time operations
 */
object TimeUtils {
    fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return when {
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, secs)
            minutes > 0 -> String.format("%d:%02d", minutes, secs)
            else -> String.format("0:%02d", secs)
        }
    }

    fun formatDurationMinutes(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60

        return when {
            hours > 0 -> "${hours}h ${mins}m"
            else -> "${mins}m"
        }
    }

    fun secondsToMinutes(seconds: Int): Int {
        return (seconds / 60.0).roundToInt()
    }

    fun minutesToSeconds(minutes: Int): Int {
        return minutes * 60
    }
}

/**
 * Utility functions for validation
 */
object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }

    fun isValidAge(age: Int): Boolean {
        return age in 13..120
    }

    fun isValidWeight(weight: Float): Boolean {
        return weight > 0 && weight <= 1000 // assuming max 1000kg/2200lbs
    }

    fun isValidHeight(height: Float): Boolean {
        return height > 0 && height <= 300 // assuming max 300cm/~10ft
    }

    fun isValidWorkoutName(name: String): Boolean {
        return name.isNotBlank() && name.length <= Constants.MAX_WORKOUT_NAME_LENGTH
    }

    fun isValidExerciseName(name: String): Boolean {
        return name.isNotBlank() && name.length <= Constants.MAX_EXERCISE_NAME_LENGTH
    }

    fun isValidReps(reps: Int): Boolean {
        return reps in 1..1000
    }

    fun isValidWeight(weight: Float, allowZero: Boolean = false): Boolean {
        return if (allowZero) weight >= 0 && weight <= 1000 else weight > 0 && weight <= 1000
    }

    fun isValidDuration(duration: Int): Boolean {
        return duration > 0 && duration <= 86400 // max 24 hours in seconds
    }
}

/**
 * Utility functions for calculations
 */
object CalculationUtils {
    /**
     * Calculate BMI (Body Mass Index)
     */
    fun calculateBMI(weightKg: Float, heightCm: Float): Float {
        val heightM = heightCm / 100
        return weightKg / (heightM * heightM)
    }

    /**
     * Get BMI category
     */
    fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal weight"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }

    /**
     * Estimate calories burned during workout (very basic estimation)
     */
    fun estimateCaloriesBurned(
        weightKg: Float,
        durationMinutes: Int,
        intensity: WorkoutIntensity = WorkoutIntensity.MODERATE
    ): Int {
        val met = when (intensity) {
            WorkoutIntensity.LOW -> 3.0
            WorkoutIntensity.MODERATE -> 5.0
            WorkoutIntensity.HIGH -> 8.0
            WorkoutIntensity.VERY_HIGH -> 12.0
        }

        // Calories = MET × weight in kg × time in hours
        return ((met * weightKg * (durationMinutes / 60.0))).roundToInt()
    }

    /**
     * Calculate one-rep max using Epley formula
     */
    fun calculateOneRepMax(weight: Float, reps: Int): Float {
        if (reps == 1) return weight
        return weight * (1 + reps / 30.0f)
    }

    /**
     * Calculate target weight for given reps based on one-rep max
     */
    fun calculateTargetWeight(oneRepMax: Float, targetReps: Int): Float {
        if (targetReps == 1) return oneRepMax
        return oneRepMax / (1 + targetReps / 30.0f)
    }
}

/**
 * Enum for workout intensity levels
 */
enum class WorkoutIntensity {
    LOW, MODERATE, HIGH, VERY_HIGH
}

/**
 * Result wrapper for operations that can fail
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Extension functions for Result
 */
inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (exception: Throwable) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}

/**
 * Extension functions for common operations
 */
fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun Float.roundToDecimals(decimals: Int): Float {
    var multiplier = 1.0f
    repeat(decimals) { multiplier *= 10 }
    return (this * multiplier).roundToInt() / multiplier
}