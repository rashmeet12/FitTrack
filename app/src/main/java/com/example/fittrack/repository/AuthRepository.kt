package com.example.fittrack.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    // Sign in with email and password
    suspend fun signInWithEmail(email: String, password: String): FirebaseUser? {
        // The await() extension suspends until the task completes or throws
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    // Sign in with Google ID token obtained via Credential Manager
    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user
    }

    suspend fun signUpWithEmail(email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    // Get currently signed-in user (or null)
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    // Sign out
    fun signOut() {
        firebaseAuth.signOut()
    }
}
