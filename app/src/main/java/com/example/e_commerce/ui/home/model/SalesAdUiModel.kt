package com.example.e_commerce.ui.home.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SalesUiAdModel(
    val id: String? = null,
    val title: String? = null,
    var productId: String? = null,
    var imageUrl: String? = null,
    var categoryId: String? = null,
    val type: String? = null,
    var externalLink: String? = null,
    var endAt: Date? = null
)

enum class SalesAdType {
    PRODUCT,
    CATEGORY,
    EXTERNAL_LINK,
    EMPTY
}