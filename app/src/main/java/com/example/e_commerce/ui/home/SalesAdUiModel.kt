package com.example.e_commerce.ui.home

data class SalesUiAdModel(
    val image: Int,
    val title: String,
    val imageUrl: String
)

enum class SalesAdType {
    PRODUCT,
    CATEGORY,
    EXTERNAL_LINK,
    EMPTY
}