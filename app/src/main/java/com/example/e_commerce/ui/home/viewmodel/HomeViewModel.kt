package com.example.e_commerce.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import com.example.e_commerce.data.models.products.ProductSaleType
import com.example.e_commerce.data.models.sales_ad.SalesAdModel
import com.example.e_commerce.data.models.user.CountryData
import com.example.e_commerce.data.repository.category.CategoryRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepository
import com.example.e_commerce.data.repository.products.ProductRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.domain.models.toProductUIModel
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import com.example.e_commerce.ui.products.models.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val salesAdsRepository: SalesAdsRepository,
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _saleProductState =
        MutableStateFlow<Resource<List<ProductModel>>>(Resource.Loading())
    val saleProductState = _saleProductState.asStateFlow()
    val salesAdsStateTamp: StateFlow<Resource<List<SalesUiAdModel>>> =
        salesAdsRepository.getSalesAds().stateIn(
            viewModelScope + IO,
            SharingStarted.Eagerly,
            Resource.Loading()
        )

    fun stopTimer() {
        salesAdsStateTamp.value.data?.forEach { it.stopCountdown() }
    }

    fun startTimer() {
        salesAdsStateTamp.value.data?.forEach { it.startCountdown() }
    }

    private val _categoryState =
        MutableStateFlow<Resource<List<CategoryUIModel>>>(Resource.Loading())
    val categoryState = _categoryState.asStateFlow()

    private val countryState = userPreferenceRepository.getUserCountry().stateIn(
        viewModelScope + IO,
        started = SharingStarted.Eagerly,
        initialValue = CountryData.getDefaultInstance()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val flashSaleState = countryState.mapLatest { country ->
        productRepository.getSaleProducts(
            country.id ?: "0",
            ProductSaleType.FLASH_SALE.type,
            10
        )

    }.mapLatest {
        it.first().map { getProductModel(it) }
    }.stateIn(
        viewModelScope + IO,
        SharingStarted.Eagerly,
        emptyList()
    )

    private fun getProductModel(productModel: ProductModel): ProductUIModel{
        val productUiModel = productModel.toProductUIModel().copy(
            currencySymbol = countryState.value.currencySymbol,
        )

        return productUiModel
    }

    init {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { source ->
                _categoryState.value = source
            }
        }
    }

    fun getSaleProduct() =
        viewModelScope.launch(IO) {
            val countryId = userPreferenceRepository.getUserCountry().first()
            productRepository.getSaleProducts(
                countryId.id,
                ProductSaleType.FLASH_SALE.type,
                10
            ).collectLatest { source ->
                Log.d("HomeViewModel", "getSaleProduct: $source")
            }
        }
}
