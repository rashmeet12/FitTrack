package com.example.fittrack.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.data.model.UserEntity
import com.example.fittrack.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class ProfileSetupUiState(
    val displayName: String = "",
    val profileImageUri: Uri? = null,
    val dateOfBirth: Long? = null,
    val age: Int? = null,
    val weightKg: Float? = null,
    val heightCm: Float? = null,
    val gender: String? = null,
    val fitnessGoal: String? = null,
    val activityLevel: String? = null,
    val isLoading: Boolean = false,
    val isProfileCompleted: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    init {
        loadExistingProfile()
    }

    private fun loadExistingProfile() = viewModelScope.launch {
        currentUser?.let { user ->
            try {
                val existingUser = userRepository.getUser(user.uid)
                existingUser?.let { userEntity ->
                    _uiState.value = _uiState.value.copy(
                        displayName = userEntity.displayName ?: "",
                        profileImageUri = userEntity.photoUrl?.let { Uri.parse(it) },
                        dateOfBirth = userEntity.dateOfBirth,
                        age = userEntity.age,
                        weightKg = userEntity.weight,
                        heightCm = userEntity.height,
                        gender = userEntity.gender,
                        fitnessGoal = userEntity.fitnessGoal,
                        activityLevel = userEntity.activityLevel
                    )
                    Log.d("ProfileSetupViewModel", "Loaded existing profile: ${userEntity.displayName}")
                }
            } catch (e: Exception) {
                Log.e("ProfileSetupViewModel", "Error loading existing profile", e)
            }
        }
    }

    fun updateDisplayName(name: String) {
        _uiState.value = _uiState.value.copy(displayName = name)
        Log.d("ProfileSetupViewModel", "Updated display name: $name")
    }

    fun updateProfileImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(profileImageUri = uri)
        Log.d("ProfileSetupViewModel", "Updated profile image URI: $uri")
    }

    fun updateDateOfBirth(dateMillis: Long) {
        Log.d("ProfileSetupViewModel", "Updating date of birth: $dateMillis")
        val age = calculateAge(dateMillis)
        _uiState.value = _uiState.value.copy(
            dateOfBirth = dateMillis,
            age = age
        )
        Log.d("ProfileSetupViewModel", "Updated date of birth: $dateMillis, calculated age: $age")
    }

    fun updateWeightKg(weightKg: Float?) {
        _uiState.value = _uiState.value.copy(weightKg = weightKg)
        Log.d("ProfileSetupViewModel", "Updated weight: $weightKg")
    }

    fun updateHeightCm(heightCm: Float?) {
        _uiState.value = _uiState.value.copy(heightCm = heightCm)
        Log.d("ProfileSetupViewModel", "Updated height: $heightCm")
    }

    fun updateGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
        Log.d("ProfileSetupViewModel", "Updated gender: $gender")
    }

    fun updateFitnessGoal(goal: String) {
        _uiState.value = _uiState.value.copy(fitnessGoal = goal)
        Log.d("ProfileSetupViewModel", "Updated fitness goal: $goal")
    }

    fun updateActivityLevel(level: String) {
        _uiState.value = _uiState.value.copy(activityLevel = level)
        Log.d("ProfileSetupViewModel", "Updated activity level: $level")
    }

    fun completeProfile() = viewModelScope.launch {
        Log.d("ProfileSetupViewModel", "Starting profile completion")
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        try {
            currentUser?.let { user ->
                val currentState = _uiState.value

                // Validate required fields
                if (currentState.displayName.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Display name is required"
                    )
                    return@launch
                }

                val existingUser = userRepository.getUser(user.uid)
                val updatedUser = existingUser?.copy(
                    displayName = currentState.displayName,
                    photoUrl = currentState.profileImageUri?.toString(), // FIX: Save profile image URI
                    dateOfBirth = currentState.dateOfBirth,
                    age = currentState.age,
                    weight = currentState.weightKg,
                    height = currentState.heightCm,
                    gender = currentState.gender,
                    fitnessGoal = currentState.fitnessGoal,
                    activityLevel = currentState.activityLevel,
                    isProfileComplete = true
                ) ?: UserEntity(
                    userId = user.uid,
                    email = user.email.orEmpty(),
                    displayName = currentState.displayName,
                    photoUrl = currentState.profileImageUri?.toString(), // FIX: Save profile image URI
                    dateOfBirth = currentState.dateOfBirth,
                    age = currentState.age,
                    weight = currentState.weightKg,
                    height = currentState.heightCm,
                    gender = currentState.gender,
                    fitnessGoal = currentState.fitnessGoal,
                    activityLevel = currentState.activityLevel,
                    isProfileComplete = true
                )

                Log.d("ProfileSetupViewModel", "About to save user: $updatedUser")
                userRepository.upsertUser(updatedUser)
                Log.d("ProfileSetupViewModel", "Profile saved successfully")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isProfileCompleted = true
                )
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "User not authenticated"
                )
            }
        } catch (e: Exception) {
            Log.e("ProfileSetupViewModel", "Error completing profile", e)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to save profile: ${e.message}"
            )
        }
    }

    private fun calculateAge(birthDateMillis: Long): Int {
        return try {
            val birthDate = Calendar.getInstance().apply {
                timeInMillis = birthDateMillis
            }
            val today = Calendar.getInstance()

            var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

            // Check if birthday hasn't occurred this year yet
            if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            // Ensure age is reasonable (0-150)
            age.coerceIn(0, 150)
        } catch (e: Exception) {
            Log.e("ProfileSetupViewModel", "Error calculating age", e)
            0
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}