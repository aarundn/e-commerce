package com.example.e_commerce.ui.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.data.repository.auth.CountriesRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.domain.models.toUIModel
import com.example.e_commerce.ui.login.models.CountryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CountriesViewModel @Inject constructor(
    countryRepository : CountriesRepository,
    private val userPreferenceRepository: UserPreferenceRepository
): ViewModel() {

    private val countriesState = countryRepository.getCountries().stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val countriesUIModelState = countriesState.mapLatest { countries ->
        countries.map { country ->
            Log.d("CountriesViewModel", "countriesUIModelState: $country")
            country.toUIModel()
        }
    }
    fun saveCountry(country: CountryUiModel) {
        viewModelScope.launch {
            userPreferenceRepository.saveUserCountry(country)
        }
    }
}