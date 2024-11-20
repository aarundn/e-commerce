package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.ui.login.models.CountryUiModel


fun CountryModel.toUIModel(): CountryUiModel {
    return CountryUiModel(
        id = id,
        name = name,
        code = code,
        currency = currency,
        currencySymbol = currency_symbol,
        flagUrl = flag_url
    )
}