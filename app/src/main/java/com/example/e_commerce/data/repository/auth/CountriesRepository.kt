package com.example.e_commerce.data.repository.auth

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.ui.login.models.CountryUiModel
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {
    fun getCountries(): Flow<List<CountryModel>>
}