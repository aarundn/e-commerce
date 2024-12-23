package com.example.e_commerce.data.models.auth

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CountryModel(
    val id: String? = null,
    val name: String? = null,
    val code: String? = null,
    val currency: String? = null,
    @get:PropertyName("currency_symbol")
    @set:PropertyName("currency_symbol")
    var currencySymbol: String? = null,

    @get:PropertyName("flag_url")
    @set:PropertyName("flag_url")
    var flagUrl : String? = null

): Parcelable
