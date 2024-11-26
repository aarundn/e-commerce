package com.example.e_commerce.data.repository.products

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import com.example.e_commerce.ui.products.models.ProductUIModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getCategoryProducts(
        categoryId: String,
        pageLimit: Int
    ): Flow<Resource<List<ProductModel>>>

    suspend fun getSaleProducts(
        countryId: String,
        saleType: String,
        pageLimit: Int
    ): Flow<Resource<List<ProductUIModel>>>
}