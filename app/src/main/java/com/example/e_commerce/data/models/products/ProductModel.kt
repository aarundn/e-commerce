package com.example.e_commerce.data.models.products

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ProductModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,

    @get:PropertyName("categories_id")
    @set:PropertyName("categories_id")
    var categoriesIDs: List<String>? = null,

    var images: List<String>? = null,
    var price: Int? = null,
    var quantity: Int? = null,
    var rate: Float? = null,
    @get:PropertyName("offer_perecentage")
    @set:PropertyName("offer_perecentage")
    var offerPercentage: Int? = null,

    @get:PropertyName("sale_type")
    @set:PropertyName("sale_type")
    var saleType: String? = null,
    var colors: List<ProductColorModel>? = null,
    var sizes: List<ProductSizeModel>? = null,
) : Parcelable
@Keep
@Parcelize
data class ProductColorModel(
    var size: Long? = null,
    var stock: Int? = null,
    var color: String? = null
) : Parcelable

@Keep
@Parcelize
data class ProductSizeModel(
    var size: Long? = null,
    var stock: Int? = null
) : Parcelable

enum class ProductSaleType(val type: String) {
    FLASH_SALE("flash_sale"),
    MEGA_SALE("mega_sale"),

}