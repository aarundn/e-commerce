package com.example.e_commerce.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel: ViewModel() {
    private val search = MutableStateFlow("")
}