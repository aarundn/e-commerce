package com.example.e_commerce.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow

class ForgotPasswordViewModel(
    private val firebaseAuthRepository: FireBaseAuthRepository
): ViewModel() {
    val email = MutableStateFlow("")
    fun sendPasswordResetEmail(email: String) {

    }
    companion object {

        const val TAG = "ForgetPasswordViewModel"
    }
}

class ForgotPasswordViewModelFactory(
    private val contextValue: Context
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            return ForgotPasswordViewModel(
                firebaseAuthRepository = FireBaseAuthRepositoryImpl()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}