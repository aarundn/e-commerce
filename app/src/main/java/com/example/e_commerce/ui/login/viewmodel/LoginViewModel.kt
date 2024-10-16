package com.example.e_commerce.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import com.example.e_commerce.data.repository.user.UserPreferenceRepository

class LoginViewModel(
    private val userRepository: UserPreferenceRepository
): ViewModel() {

    suspend fun isUserLoggedIn() = userRepository.isUserLoggedIn()
    suspend fun saveUserState(isLoggedIn: Boolean) = userRepository.saveUserState(isLoggedIn)
}