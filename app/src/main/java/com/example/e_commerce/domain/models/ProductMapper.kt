package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.products.ProductModel
import com.example.e_commerce.ui.products.models.ProductUIModel


fun ProductUIModel.toProductModel(): ProductModel {
    return ProductModel(
        id = id,
        name = name,
        description = description,
        categoriesIDs = categoriesIDs,
        images = images,
        price = price,
        quantity = quantity,
        rate = rate,
        offerPercentage = offerPercentage,
        saleType = saleType,
        colors = colors
    )
}
fun ProductModel.toProductUIModel(): ProductUIModel {
    return ProductUIModel(
        id = id,
        name = name,
        description = description,
        categoriesIDs = categoriesIDs,
        images = images,
        price = price,
        quantity = quantity,
        rate = rate ?: 0f,
        offerPercentage = offerPercentage,
        saleType = saleType,
        colors = colors,
    )
}