package com.example.e_commerce.data.repository.products

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getCategoryProducts(
        categoryId: String,
        pageLimit: Int
    ): Flow<Resource<List<ProductModel>>>

    fun getSaleProducts(
        countryId: String,
        saleType: String,
        pageLimit: Int
    ): Flow<List<ProductModel>>
}