package com.example.e_commerce.data.repository.category

import android.util.Log
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.category.CategoryModel
import com.example.e_commerce.domain.models.toUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): CategoryRepository {
    override fun getCategories() = flow {
        emit(Resource.Loading())
        try {
            val categories = firestore.collection("category").get().await().toObjects(
                CategoryModel::class.java)
            // repeat categories item 10 times
//                val repeatCategories = mutableListOf<CategoryModel>()
//                repeat(10) {
//                    repeatCategories.addAll(categories)
//                }
            emit(Resource.Success(categories.map { it.toUIModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(Exception(e.message)))

        }
    }
}