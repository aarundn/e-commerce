package com.example.e_commerce.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.repository.user.UserRepositoryDataSourceImpl
import com.example.e_commerce.ui.common.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepositoryDataSourceImpl
) : ViewModel() {

    suspend fun isLogged() = userRepository.isUserLoggedIn()

    fun setUserStateLoggedIn(isLoggedIn: Boolean){
        viewModelScope.launch {
            userRepository.saveUserState(isLoggedIn)
        }
    }
}
class UserViewModelFactory(private val userRepository: UserRepositoryDataSourceImpl) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Ensure that the class being requested is the UserViewModel
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            // Return a new instance of UserViewModel with the userRepository passed in
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
