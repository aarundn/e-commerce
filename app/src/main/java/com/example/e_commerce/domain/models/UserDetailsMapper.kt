package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.user.CountryData
import com.example.e_commerce.data.models.user.UserDetailsModel
import com.example.e_commerce.data.models.user.UserDetailsPreferences
import com.example.e_commerce.ui.login.models.CountryUiModel

fun UserDetailsPreferences.toUserDetailsModel(): UserDetailsModel {
    return UserDetailsModel(
        id = id,
        email = email,
        name = name,
        reviews = reviewsList
    )
}

fun UserDetailsModel.toUserDetailsPreferences(countryData: CountryData): UserDetailsPreferences {
    return UserDetailsPreferences.newBuilder()
        .setId(id)
        .setEmail(email)
        .setName(name)
        .addAllReviews(reviews?.toList() ?: emptyList())
        .setCountry(countryData)
        .build()
}