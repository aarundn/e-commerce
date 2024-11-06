package com.example.e_commerce.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.datasource.datastore.AppPreferencesDataSource
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepositoryImpl
import com.example.e_commerce.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: FireBaseAuthRepository
) : ViewModel() {

    private val _registerState = MutableSharedFlow<Resource<UserDetailsModel>>()
    val registerState: SharedFlow<Resource<UserDetailsModel>> = _registerState.asSharedFlow()

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    private val isRegisterIsValid = combine(
        name, email, password, confirmPassword
    ) { name, email, password, confirmPassword ->
        email.isEmailValid() && name.isNotEmpty() &&
                password.length >= 6 && confirmPassword.isNotEmpty()
                && confirmPassword == password

    }

    fun registerWithEmailAndPassWord() = viewModelScope.launch(IO) {
        val name = name.value
        val email = email.value
        val password = password.value
        val confirmPassword = confirmPassword.value
        if (isRegisterIsValid.first()) {
            authRepository.registerWithEmailAndPassWord(name, email, password).collect {
                when (it) {
                    is Resource.Success -> {
                        _registerState.emit(Resource.Success(it.data))
                    }

                    is Resource.Error -> {
                        _registerState.emit(Resource.Error(it.exception ?: Exception("An unknown error occurred")))
                    }

                    is Resource.Loading -> {
                       _registerState.emit(Resource.Loading())
                    }
                }
            }
        } else {
            _registerState.emit(Resource.Error(Exception("Invalid email or password")))
        }
    }


}
