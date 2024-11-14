package com.example.e_commerce.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.sales_ad.SalesAdModel
import com.example.e_commerce.data.repository.category.CategoryRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepository
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val salesAdsRepository: SalesAdsRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    val salesAdsStateTamp: StateFlow<Resource<List<SalesUiAdModel>>> = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO,
            SharingStarted.Eagerly,
        Resource.Loading())

    fun stopTimer() {
        salesAdsStateTamp.value.data?.forEach { it.stopCountdown() }
    }
    fun startTimer() {
        salesAdsStateTamp.value.data?.forEach { it.startCountdown() }
    }

    private val _categoryState = MutableStateFlow<Resource<List<CategoryUIModel>>>(Resource.Loading())
    val categoryState = _categoryState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { source ->
                _categoryState.value = source
            }
        }
    }
}
