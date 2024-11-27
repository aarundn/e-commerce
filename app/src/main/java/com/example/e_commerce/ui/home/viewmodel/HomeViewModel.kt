package com.example.e_commerce.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import com.example.e_commerce.data.models.products.ProductSaleType
import com.example.e_commerce.data.models.sales_ad.SalesAdModel
import com.example.e_commerce.data.models.user.CountryData
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
import com.google.firebase.firestore.DocumentSnapshot
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
    private val userPreferenceRepository: UserPreferenceRepository,
    private val specialSectionsRepository: SpecialSectionsRepository
) : ViewModel() {


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

    @OptIn(ExperimentalCoroutinesApi::class)
    val isRecommendedSection = recommendedDataState.mapLatest { it == null }.asLiveData()


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getProductsSale(productSaleType: ProductSaleType): StateFlow<List<ProductUIModel>> =
        countryState.mapLatest { country ->
            productRepository.getSaleProducts(
                country.id ?: "0",
                productSaleType.type,
                10
            )
        }.mapLatest {
            it.first().map { getProductModel(it) }
        }.stateIn(
            viewModelScope + IO,
            SharingStarted.Eagerly,
            emptyList()
        )

    val flashSaleState = getProductsSale(ProductSaleType.FLASH_SALE)
    val megaSaleState = getProductsSale(ProductSaleType.MEGA_SALE)


    val isEmptyFlashSale = flashSaleState.map { it.isEmpty() }.asLiveData()
    val isEmptyMegaSale = megaSaleState.map { it.isEmpty() }.asLiveData()
    private fun getProductModel(productModel: ProductModel): ProductUIModel {
        val productUiModel = productModel.toProductUIModel().copy(
            currencySymbol = countryState.value.currencySymbol,
        )

        return productUiModel
    }

    private val _allProductsState: MutableStateFlow<List<ProductUIModel>> = MutableStateFlow(emptyList())
    val allProductsState = _allProductsState.asStateFlow()
    val isLoadingAllProducts = MutableStateFlow(false)
    val isFinishedLoadingAllProducts = MutableStateFlow(false)
    var lastDocument: DocumentSnapshot? = null

    fun getNextProducts() = viewModelScope.launch(IO) {
        if (isLoadingAllProducts.value) return@launch
        if (isFinishedLoadingAllProducts.value) return@launch
        isLoadingAllProducts.emit(true)
        val countryId = countryState.first().id ?: "0"
        productRepository.getAllProductPaging(
            countryId,
            4,
            lastDocument
        ).collectLatest { resource ->
            when(resource){
                is Resource.Loading -> isLoadingAllProducts.emit(true)
                is Resource.Success -> {
                    isLoadingAllProducts.emit(false)
                    resource.data?.let { document ->
                        if (document.isEmpty){
                            isFinishedLoadingAllProducts.emit(true)
                            return@collectLatest
                        } else{
                            lastDocument = document.documents.lastOrNull()
                            _allProductsState.value = document.toObjects(ProductModel::class.java)
                                .map { getProductModel(it) }
                        }
                    }
                }
                is Resource.Error ->{
                    isLoadingAllProducts.emit(false)
                    Log.d(TAG,"data is : ${resource.exception?.message}")
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { source ->
                _categoryState.value = source
            }
        }
    }
    companion object {
        private const val TAG = "HomeViewModel"
    }

}
