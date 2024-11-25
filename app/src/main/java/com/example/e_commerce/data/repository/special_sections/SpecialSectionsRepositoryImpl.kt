package com.example.e_commerce.data.repository.special_sections

import com.example.e_commerce.data.models.SpecialSectionModel
import com.example.e_commerce.data.models.SpecialSections
import com.example.e_commerce.utils.CrashlyticsUtils
import com.example.e_commerce.utils.SpecialSectionsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpecialSectionsRepositoryImpl @Inject constructor(
    val firestore: FirebaseFirestore
) : SpecialSectionsRepository {
    override fun getRecommendedProducts() = flow {
        try {
            val recommendedProducts = firestore.collection("special_sections")
                .document(SpecialSections.RECOMMENDED_PRODUCTS.id)
                .get()
                .await()
                .toObject(SpecialSectionModel::class.java)
            emit(recommendedProducts)

        } catch (e: Exception) {
            flow {
                val msg = e.message ?: "An error occurred"
                CrashlyticsUtils.sendCustomLogToCrashlytics<SpecialSectionsException>(
                    msg,
                    Pair(CrashlyticsUtils.SPECIAL_SECTIONS, msg)
                )
                emit(e.message ?: "An error occurred")
            }
        }
    }
}