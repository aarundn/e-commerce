package com.example.e_commerce.data.repository.user

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    suspend fun isUserLoggedIn(): Flow<Boolean>
    suspend fun saveUserState(isLoggedIn: Boolean)
}