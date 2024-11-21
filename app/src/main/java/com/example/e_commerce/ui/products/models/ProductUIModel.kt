package com.example.e_commerce.ui.products.models

data class ProductUIModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var categoriesIDs: List<String>? = null,
    var images: List<String>?,
    var price: Int?,
    var quantity: Int?,
    var priceAfterSale: Int?,
    var rate: Double? = null,
    var offerPercentage: Int? = null,
    var saleType: String? = null,
    var colors: List<String>? = null,
    var currencySymbol : String? = ""
)
