package com.example.e_commerce.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserRepositoryDataSourceImpl
import com.example.e_commerce.ui.common.viewmodel.UserViewModel

class LoginViewModel(
    private val userRepository: UserPreferenceRepository
): ViewModel() {

    suspend fun isUserLoggedIn() = userRepository.isUserLoggedIn()
    suspend fun saveUserState(isLoggedIn: Boolean) = userRepository.saveUserState(isLoggedIn)
}

class LoginViewModelFactory(private val userRepository: UserRepositoryDataSourceImpl) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Ensure that the class being requested is the UserViewModel
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // Return a new instance of UserViewModel with the userRepository passed in
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}