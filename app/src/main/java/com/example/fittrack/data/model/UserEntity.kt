package com.example.fittrack.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val userId: String,           // Firebase UID
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis() ,   // age/dob , weight, height , gender, goal

    // New profile fields
    val dateOfBirth: Long? = null, // Store as timestamp
    val age: Int? = null,
    val weight: Float? = null, // in kg
    val height: Float? = null, // in cm
    val gender: String? = null, // "Male", "Female", "Other"
    val fitnessGoal: String? = null, // "Weight Loss", "Muscle Gain", "Maintenance", etc.
    val activityLevel: String? = null, // "Sedentary", "Lightly Active", "Moderately Active", "Very Active"
    val isProfileComplete: Boolean = false

)