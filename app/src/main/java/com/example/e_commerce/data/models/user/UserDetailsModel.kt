package com.example.e_commerce.data.models.user

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class UserDetailsModel(
    @ServerTimestamp
    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt: Long? = null,
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var disabled: Boolean? = null,
    var reviews: List<String>? = null,
    var idToken: String? = null
) : Parcelable

