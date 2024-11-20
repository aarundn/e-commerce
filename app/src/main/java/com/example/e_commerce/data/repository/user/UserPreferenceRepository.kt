package com.example.e_commerce.data.repository.user

import com.example.e_commerce.data.models.user.CountryData
import com.example.e_commerce.data.models.user.UserDetailsPreferences
import com.example.e_commerce.ui.login.models.CountryUiModel
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    fun getUserDetails(): Flow<UserDetailsPreferences>
    suspend fun updateUserId(userId: String)
    suspend fun getUserId(): Flow<String>
    suspend fun clearUserPreferences()
    suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences)
    suspend fun saveUserCountry(countryId: CountryUiModel)
    fun getUserCountry():  Flow<CountryData>
}