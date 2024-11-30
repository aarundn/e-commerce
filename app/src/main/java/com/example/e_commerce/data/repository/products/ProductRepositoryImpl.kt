package com.example.e_commerce.data.repository.products

import android.util.Log
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.products.ProductModel
import com.example.e_commerce.domain.models.toProductUIModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override fun getAllProductPaging(
        countryId: String,
        pageLimit: Int,
        lastDocument: DocumentSnapshot?
    ): Flow<Resource<QuerySnapshot>> =  flow {
        try {
            emit(Resource.Loading())
            var firstQuery = firestore.collection("products").orderBy("price")
            if (lastDocument != null) {
                firstQuery = firstQuery.startAfter(lastDocument)
            }
            firstQuery = firstQuery.limit(pageLimit.toLong())
            val products = firstQuery.get().await()
            emit(Resource.Success(products))
        } catch (e: Exception) {
            Log.d(TAG, "getAllProductPaging: ${e.message}")
            emit(Resource.Error(Exception(e.message)))
        }

    }

    override fun listenToProductDetails(productId: String): Flow<ProductModel> {
        return callbackFlow {
            val listener = firestore.collection("products")
                .document(productId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    val product = value?.toObject(ProductModel::class.java)
                    if (product != null) {
                        trySend(product)
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    companion object {
        private const val TAG = "ProductRepositoryImpl"
    }
}