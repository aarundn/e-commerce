package com.example.e_commerce.data.models.products

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ProductModel (
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,

    @get:PropertyName("categories_ids")
    @set:PropertyName("categories_ids")
    var categoriesIDs: List<String>? = null,

    var images: List<String>? = null,
    var price: Int? = null,
    var rate: Float? = null,


):Parcelable
