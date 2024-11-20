package com.example.e_commerce.data.models.auth

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CountryModel(
    val id: String? = null,
    val name: String? = null,
    val code: String? = null,
    val currency: String? = null,
    val currency_symbol: String? = null,
    val flag_url : String? = null

): Parcelable
