package com.example.e_commerce.data.repository.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.e_commerce.data.datasource.datastore.DataStoreKeys.IS_USER_LOGGED_IN
import com.example.e_commerce.data.datasource.datastore.appDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryDataSourceImpl(private var context: Context): UserPreferenceRepository {

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return context.appDataStore.data.map { preferences ->
            preferences[IS_USER_LOGGED_IN] ?: false
        }
    }

    override suspend fun saveUserState(isLoggedIn: Boolean) {
        context.appDataStore.edit { preference ->
            preference[IS_USER_LOGGED_IN] = isLoggedIn
        }
    }
}