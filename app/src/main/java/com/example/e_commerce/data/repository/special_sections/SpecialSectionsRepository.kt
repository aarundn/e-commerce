package com.example.e_commerce.data.repository.special_sections

import com.example.e_commerce.data.models.SpecialSectionModel
import kotlinx.coroutines.flow.Flow

interface SpecialSectionsRepository {
     fun getRecommendedProducts(): Flow<SpecialSectionModel?>
}