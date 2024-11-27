package com.example.e_commerce.ui.products.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.e_commerce.data.models.products.ProductColorModel
import com.example.e_commerce.data.models.products.ProductSizeModel
import kotlinx.parcelize.Parcelize

data class ProductUIModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var categoriesIDs: List<String>? = null,
    var images: List<String>?,
    var price: Int?,
    var quantity: Int?,
    var rate: Float? = null,
    var offerPercentage: Int? = null,
    var saleType: String? = null,
    val colors: List<ProductColorModel>?,
    val sizes: List<ProductSizeModel>?,
    var currencySymbol: String? = ""
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
@Keep
@Parcelize
data class ProductColorUIModel(
    var size: Long? = null, var stock: Int? = null, var color: String? = null
) : Parcelable


@Keep
@Parcelize
data class ProductSizeUIModel(
    var size: Long? = null, var stock: Int? = null
) : Parcelable