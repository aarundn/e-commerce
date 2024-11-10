package com.example.e_commerce.data.models.sales_ad

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SalesAdModel(
    val id: String? = null,
    val title: String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId: String? = null,

    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String? = null,

    @get:PropertyName("category")
    @set:PropertyName("category")
    var categoryId: String? = null,

    val type: String? = null,
    @get:PropertyName("externel_link")
    @set:PropertyName("externel_link")
    var externalLink: String? = null,

    @ServerTimestamp
    @get:PropertyName("end_at")
    @set:PropertyName("end_at")
    var endAt: Long? = null
) : Parcelable
