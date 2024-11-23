package com.example.e_commerce.ui.products.models

data class ProductUIModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var categoriesIDs: List<String>? = null,
    var images: List<String>?,
    var price: Int?,
    var quantity: Int?,
    var rate: Double? = null,
    var offerPercentage: Int? = null,
    var saleType: String? = null,
    var colors: List<String>? = null,
    var currencySymbol : String? = ""
){
    fun getFormattedPrice(): String {
        return "$price $currencySymbol"
    }
    fun getFormattedPriceAfterSale(): String {
        if (saleType == null) return getFormattedPrice()
        val newPrice = offerPercentage?.let{price?.minus((price!! * it) / 100)}
        return "$newPrice $currencySymbol"
    }
    fun getFormatedOfferPercentage(): String {
        return "$offerPercentage%"
    }

    fun getFirstImage(): String {
        return images?.firstOrNull() ?: ""
    }
}
