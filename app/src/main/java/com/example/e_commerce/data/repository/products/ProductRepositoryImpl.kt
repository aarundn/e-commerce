package com.example.e_commerce.data.repository.products

import android.util.Log
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val  firestore: FirebaseFirestore
): ProductRepository {
    override fun getCategoryProducts(
        categoryId: String,
        pageLimit: Int
    ): Flow<Resource<List<ProductModel>>> = flow {
        emit(Resource.Loading())
        try {
            val products = firestore.collection("products")
                .whereEqualTo("category_id", categoryId)
                .limit(pageLimit.toLong())
                .get()
                .await()
                .toObjects(ProductModel::class.java)
            emit(Resource.Success(products))
        } catch (e: Exception) {
            emit(Resource.Error(Exception(e.message)))
        }
    }

    override fun getSaleProducts(
        countryId: String,saleType:String, pageLimit: Int
    ):Flow<List<ProductModel>>
    {
        return flow {

            val products = firestore.collection("products")
                .whereEqualTo("sale_type", saleType)
                .whereEqualTo("country_id", countryId)
                .orderBy("price")
                .limit(pageLimit.toLong())
                .get()
                .await()
                .toObjects(ProductModel::class.java)

            emit(products)
            Log.d("ProductRepositoryImpl", "getSaleProducts: $products")

        }
    }


}