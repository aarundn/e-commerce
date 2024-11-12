package com.example.e_commerce.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.sales_ad.SalesAdModel
import com.example.e_commerce.data.repository.home.SalesAdsRepository
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
    private val salesAdsRepository: SalesAdsRepository
): ViewModel() {

    val salesAdsStateTamp: StateFlow<Resource<List<SalesAdModel>>> = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO,
            SharingStarted.Eagerly,
        Resource.Loading())

//    private var _salesAds : MutableStateFlow<Resource<List<SalesAdModel>>> =
//        MutableStateFlow(Resource.Loading())
//    val salesAds = _salesAds.asStateFlow()
//
//    init {
//        getSalesAds()
//    }
//
//    private fun getSalesAds()  = viewModelScope.launch(IO) {
//        salesAdsRepository.getSalesAds().collectLatest {
//            _salesAds.value = it
//        }
//    }

}