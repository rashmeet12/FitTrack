package com.example.fittrack.repository

import com.example.fittrack.CurrentUserPreference
import com.example.fittrack.data.dao.UserDao
import com.example.fittrack.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val currentUserPref: CurrentUserPreference
) {
        suspend fun upsertUser(user: UserEntity) {
            userDao.insert(user)
        }

        suspend fun getUser(uid: String): UserEntity? {
            return userDao.getById(uid)
        }

        // If you still store something for session:
        suspend fun saveCurrentUser(id: String) {
            currentUserPref.saveUserId(id)
        }
        fun getCurrentUser(): Flow<String?> = currentUserPref.userIdFlow
}