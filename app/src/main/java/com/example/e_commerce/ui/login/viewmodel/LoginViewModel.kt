package com.example.e_commerce.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserRepositoryDataSourceImpl
import com.example.e_commerce.ui.common.viewmodel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserPreferenceRepository,
    private val firebaseRepo: FireBaseAuthRepository
): ViewModel() {
    var email = MutableStateFlow("")
    var password = MutableStateFlow("")
    fun login(){
       var email = email.value
        var password = password.value
    }
}

class LoginViewModelFactory(
    private val userRepository: UserPreferenceRepository,
    private val firebaseRepo: FireBaseAuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Ensure that the class being requested is the UserViewModel
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // Return a new instance of UserViewModel with the userRepository passed in
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository, firebaseRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}