package com.example.e_commerce.ui.home.model

data class SalesUiAdModel(
    val title: String? = null,
    val imageUrl: String? = null,
    val endAt: Long? = null
)

enum class SalesAdType {
    PRODUCT,
    CATEGORY,
    EXTERNAL_LINK,
    EMPTY
}