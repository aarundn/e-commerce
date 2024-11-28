package com.example.e_commerce.data.repository.products

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
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
    fun getAllProductPaging(
        countryId: String,
        pageLimit: Int,
        lastDocument: DocumentSnapshot? = null
    ): Flow<Resource<QuerySnapshot>>

    fun listenToProductDetails(productId: String): Flow<ProductModel>
}