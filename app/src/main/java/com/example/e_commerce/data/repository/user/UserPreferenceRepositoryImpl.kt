package com.example.e_commerce.data.repository.user

import android.app.Application
import android.content.Context
import com.example.e_commerce.data.datasource.datastore.userDetailsDataStore
import com.example.e_commerce.data.models.user.CountryData
import com.example.e_commerce.data.models.user.UserDetailsPreferences
import com.example.e_commerce.ui.login.models.CountryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepositoryImpl @Inject constructor(val context: Application) : UserPreferenceRepository {
    override fun getUserDetails(): Flow<UserDetailsPreferences> {
        return context.userDetailsDataStore.data
    }

    override suspend fun updateUserId(userId: String) {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setId(userId).build()
        }
    }

    override suspend fun getUserId(): Flow<String> {
        return context.userDetailsDataStore.data.map { it.id }
    }

    override suspend fun clearUserPreferences() {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().clear().build()
        }
    }

    override suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences) {
        context.userDetailsDataStore.updateData { userDetailsPreferences }
    }

    override suspend fun saveUserCountry(countryId: CountryUiModel) {
        val countryData = CountryData.newBuilder()
            .setId(countryId.id)
            .setName(countryId.name)
            .setCode(countryId.code)
            .setCurrency(countryId.currency)
            .setCurrencySymbol(countryId.currencySymbol)
            .build()
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setCountry(countryData).build()
        }
    }

    override fun getUserCountry(): Flow<CountryData> {
        return context.userDetailsDataStore.data.map { it.country }
    }
}