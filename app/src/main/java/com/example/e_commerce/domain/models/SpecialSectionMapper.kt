package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.SpecialSectionModel
import com.example.e_commerce.data.models.SpecialSections
import com.example.e_commerce.ui.home.model.SpecialSectionUiModel

fun SpecialSectionUiModel.toSpecialSectionModel(): SpecialSectionModel {
    return SpecialSectionModel(
        id = id,
        title = title,
        description = description,
        type = type,
        image = image,
        enabled = enabled
    )
}
fun SpecialSectionModel.toSpecialSectionUiModel(): SpecialSectionUiModel {
    return SpecialSectionUiModel(
        id = id,
        title = title,
        description = description,
        type = type,
        image = image,
        enabled = enabled
    )
}