package com.example.e_commerce.data.repository.home

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.sales_ad.SalesAdModel
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import kotlinx.coroutines.flow.Flow

interface SalesAdsRepository {
     fun getSalesAds(): Flow<Resource<List<SalesUiAdModel>>>
}