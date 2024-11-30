package com.example.e_commerce.data.repository.auth

import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.domain.models.toUIModel
import com.example.e_commerce.ui.login.models.CountryUiModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): CountriesRepository {
    override fun getCountries(): Flow<List<CountryModel>> {
        return flow {
                val countries = firestore.collection("countries").get().await().toObjects(
                    CountryModel::class.java)
                emit(countries)

        }
    }
}