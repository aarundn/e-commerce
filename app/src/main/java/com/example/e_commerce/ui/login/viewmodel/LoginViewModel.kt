package com.example.e_commerce.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserRepositoryDataSourceImpl
import com.example.e_commerce.ui.common.viewmodel.UserViewModel
import com.example.e_commerce.utils.isEmailValid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserPreferenceRepository,
    private val firebaseRepo: FireBaseAuthRepository
): ViewModel() {

    val loginState : MutableStateFlow<Resource<String>?> = MutableStateFlow(null)
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginValid: Flow<Boolean> = combine(email, password){ email, password ->
        email.isEmailValid() && password.length >= 6

    }
    fun login(){
       val email = email.value
        val password = password.value
        viewModelScope.launch {
            if (isLoginValid.first()){
                firebaseRepo.loginWithEmailAndPassword(email, password).onEach {
                    when(it){
                        is Resource.Loading -> loginState.update { Resource.Loading() }
                        is Resource.Error -> loginState.update { Resource.Error(it?.exception ?: Exception("unknown Error ")) }
                        is Resource.Success -> {
                            userRepository.saveUserState(true)
                            loginState.update { Resource.Success("Success Login") }
                        }
                    }
                }.launchIn(viewModelScope)
            } else {
                loginState.update { Resource.Error(Exception("Invalid Email or Password")) }
            }
        }

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