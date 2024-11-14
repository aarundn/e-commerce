package com.example.e_commerce.data.models.category

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CategoryModel (
    val id: String? = null,
    val name: String? = null,
    val icon: String? = null
): Parcelable {
    override fun toString(): String {
        return  "CategoryModel(id=$id, title=$name, icon=$icon)"
    }
}