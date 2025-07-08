package com.example.fittrack.ui.auth

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.data.model.UserEntity
import com.example.fittrack.repository.AuthRepository
import com.example.fittrack.repository.UserRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthState {
    Authenticated,
    Unauthenticated,
    ProfileSetupRequired
}

data class AuthUiState(
    val authState: AuthState = AuthState.Unauthenticated,
    val user: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Tracks authenticated state: null = loading, true = signed in, false = signed out
    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()

    // Firebase Auth listener
    private val authListener = AuthStateListener { auth ->
        _isAuthenticated.value = auth.currentUser != null
        viewModelScope.launch {
            auth.currentUser?.let { user ->
                try {
                    val authState = ensureLocalProfile(user)
                    _uiState.value = _uiState.value.copy(
                        authState = authState,
                        user = user,
                        isLoading = false,
                        errorMessage = null
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        authState = AuthState.ProfileSetupRequired,
                        user = user,
                        isLoading = false,
                        errorMessage = null // Don't show error for profile setup
                    )
                }
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    authState = AuthState.Unauthenticated,
                    user = null,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
    }

    fun signInWithEmail(email: String, password: String) {
        // Input validation
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email and password are required"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val user = authRepo.signInWithEmail(email, password)
                handleAuthResult(user, "Authentication failed")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Authentication failed"
                )
            }
        }
    }

    fun signInWithGoogleCredential(credential: Credential) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                        val user = authRepo.signInWithGoogleIdToken(idToken)
                        handleAuthResult(user, "Google sign-in failed")
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Google sign-in failed"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Invalid Google credential"
                    )
                }
            }
            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unsupported credential type"
                )
            }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        // Input validation
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email and password are required"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val user = authRepo.signUpWithEmail(email, password)
                handleAuthResult(user, "Registration failed")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Registration failed"
                )
            }
        }
    }

    fun signOut() = viewModelScope.launch {
        try {
            authRepo.signOut()
            _uiState.value = AuthUiState()
            _isAuthenticated.value = false
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = e.message ?: "Sign out failed"
            )
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    // Call this after profile setup is complete
    fun completeProfileSetup() {
        val currentUser = _uiState.value.user
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    // Update the user's profile completion status
                    val existingUser = userRepo.getUser(currentUser.uid)
                    existingUser?.let { user ->
                        val updatedUser = user.copy(isProfileComplete = true)
                        userRepo.upsertUser(updatedUser)
                    }

                    // Update UI state
                    _uiState.value = _uiState.value.copy(
                        authState = AuthState.Authenticated
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = e.message ?: "Failed to complete profile setup"
                    )
                }
            }
        }
    }

    private suspend fun handleAuthResult(user: FirebaseUser?, failureMsg: String) {
        if (user != null) {
            try {
                val authState = ensureLocalProfile(user)
                _uiState.value = _uiState.value.copy(
                    authState = authState,
                    user = user,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    authState = AuthState.ProfileSetupRequired,
                    user = user,
                    isLoading = false,
                    errorMessage = null
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                authState = AuthState.Unauthenticated,
                user = null,
                isLoading = false,
                errorMessage = failureMsg
            )
        }
    }

    private suspend fun ensureLocalProfile(user: FirebaseUser): AuthState {
        val existing = userRepo.getUser(user.uid)

        if (existing == null) {
            // Create new user profile
            val newUser = UserEntity(
                userId = user.uid,
                email = user.email.orEmpty(),
                displayName = user.displayName,
                photoUrl = user.photoUrl?.toString(),
                isProfileComplete = false
            )
            userRepo.upsertUser(newUser)
            return AuthState.ProfileSetupRequired
        } else {
            // Update existing user info from Firebase if needed
            var needsUpdate = false
            var updatedUser = existing

            if (existing.email != user.email) {
                updatedUser = updatedUser.copy(email = user.email.orEmpty())
                needsUpdate = true
            }

            if (existing.displayName != user.displayName) {
                updatedUser = updatedUser.copy(displayName = user.displayName)
                needsUpdate = true
            }

            if (existing.photoUrl != user.photoUrl?.toString()) {
                updatedUser = updatedUser.copy(photoUrl = user.photoUrl?.toString())
                needsUpdate = true
            }

            if (needsUpdate) {
                userRepo.upsertUser(updatedUser)
            }

            // Check if profile setup is complete
            return if (existing.isProfileComplete) {
                AuthState.Authenticated
            } else {
                AuthState.ProfileSetupRequired
            }
        }
    }
}