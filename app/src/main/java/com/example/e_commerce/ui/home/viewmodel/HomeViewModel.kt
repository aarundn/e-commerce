package com.example.e_commerce.ui.home.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.cache.MemoryCache.ResourceRemovedListener
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import com.example.e_commerce.data.models.products.ProductSaleType
import com.example.e_commerce.data.models.sales_ad.SalesAdModel
import com.example.e_commerce.data.models.user.CountryData
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.example.e_commerce.data.repository.category.CategoryRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepository
import com.example.e_commerce.data.repository.products.ProductRepository
import com.example.e_commerce.data.repository.special_sections.SpecialSectionsRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.domain.models.toProductUIModel
import com.example.e_commerce.domain.models.toSpecialSectionUiModel
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import com.example.e_commerce.ui.products.models.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val userPreferenceRepository: UserPreferenceRepository,
    private val specialSectionsRepository: SpecialSectionsRepository
) : ViewModel() {



//    val isEmptyFlashSale = MutableStateFlow(true)
//    val isEmptyMegaSale = MutableStateFlow(true)

    private val _flashSaleState = MutableStateFlow<Resource<List<ProductUIModel>>>(Resource.Loading())
    val flashSaleState: StateFlow<Resource<List<ProductUIModel>>> = _flashSaleState.asStateFlow()
    private val _megaSaleState = MutableStateFlow<Resource<List<ProductUIModel>>>(Resource.Loading())
    val megaSaleState: StateFlow<Resource<List<ProductUIModel>>> = _megaSaleState.asStateFlow()


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
    val recommendedDataState = specialSectionsRepository.getRecommendedProducts().stateIn(
        viewModelScope + IO,
        SharingStarted.Eagerly,
        null
    ).mapLatest { it?.toSpecialSectionUiModel() }

    val isRecommendedSection = recommendedDataState.map { it == null }.asLiveData()


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getSalesProduct(
        productSaleType: ProductSaleType,
        state: MutableStateFlow<Resource<List<ProductUIModel>>>
    ) {
        viewModelScope.launch(IO) {
            countryState.collectLatest { country ->
                try {
                    state.value = Resource.Loading()
                    val products = productRepository.getSaleProducts(
                        country.id ?: "0",
                        productSaleType.type,
                        10
                    ).first()
                    state.value = Resource.Success(products.data)
                } catch (e: Exception) {
                    state.value = Resource.Error(e)
                }
            }
        }
    }


//        countryState.mapLatest { country ->
//            productRepository.getSaleProducts(
//                country.id ?: "0",
//                productSaleType.type,
//                10
//            )
//        }.mapLatest {
//             it.first().map { getProductModel(it) }


    val isEmptyFlashSale = flashSaleState.map { resource ->
        when (resource) {
            is Resource.Success -> resource.data?.isEmpty()
            is Resource.Loading -> false // Assume not empty during loading
            is Resource.Error -> true // Consider empty on error
        }
    }.asLiveData()

    val isEmptyMegaSale = megaSaleState.map { resource ->
        when (resource) {
            is Resource.Success -> resource.data?.isEmpty()
            is Resource.Loading -> false
            is Resource.Error -> true
        }
    }.asLiveData()
    private fun getProductModel(productModel: ProductModel): ProductUIModel{
        val productUiModel = productModel.toProductUIModel().copy(
            currencySymbol = countryState.value.currencySymbol,
        )

        return productUiModel
    }

    init {
        getSalesProduct(ProductSaleType.FLASH_SALE, _flashSaleState)
        getSalesProduct(ProductSaleType.MEGA_SALE, _megaSaleState)
        viewModelScope.launch {
            categoryRepository.getCategories().collect { source ->
                _categoryState.value = source
            }
        }
    }

}
@BindingAdapter("visibilities")
fun setVisibility(view: View, isEmpty: Boolean) {
    Log.d("VISIBILITIES","$isEmpty")
    view.visibility = if (isEmpty) View.GONE else View.VISIBLE
}