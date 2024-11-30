package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.category.CategoryModel
import com.example.e_commerce.ui.home.model.CategoryUIModel

fun CategoryModel.toUIModel(): CategoryUIModel {
    return CategoryUIModel(
        id = id, name = name, icon = icon
    )
}
