package com.example.e_commerce.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.utils.isEmailValid
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val firebaseAuthRepository: FireBaseAuthRepository
) : ViewModel() {

    private val _forgetPasswordState = MutableSharedFlow<Resource<String>>()
    val forgetPasswordState: SharedFlow<Resource<String>> = _forgetPasswordState.asSharedFlow()
    val email = MutableStateFlow("")
    val error = MutableStateFlow<String?>("")


    fun sendPasswordResetEmail() = viewModelScope.launch(IO) {
        if (email.value.isEmailValid()) {
            _forgetPasswordState.emit(Resource.Loading())
            firebaseAuthRepository.sendPasswordResetEmail(email.value).collect {
                _forgetPasswordState.emit(it)
            }

        } else {
            _forgetPasswordState.emit(Resource.Error(Exception("Invalid Email")))
        }
    }

    companion object {

        const val TAG = "ForgetPasswordViewModel"
    }
}

class ForgotPasswordViewModelFactory(
    private val firebaseAuthRepository: FireBaseAuthRepository = FireBaseAuthRepositoryImpl()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            return ForgotPasswordViewModel(
                firebaseAuthRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}