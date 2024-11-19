package com.example.e_commerce.data.models.products

import android.health.connect.datatypes.units.Percentage
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ProductModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,

    @get:PropertyName("categories_ids")
    @set:PropertyName("categories_ids")
    var categoriesIDs: List<String>? = null,

    var images: List<String>? = null,
    var prices: List<ProductPriceModel>? = null,
    var rate: Double? = null,

    @get:PropertyName("offer_percentage")
    @set:PropertyName("offer_percentage")
    var offerPercentage: Int? = null,
    var colors: List<String>? = null,
    ) : Parcelable {
    override fun toString(): String {
        return "ProductModel(id=$id, name=$name, description=$description, categoriesIDs=$categoriesIDs, images=$images, prices=$prices, rate=$rate)"
    }
}

@Keep
@Parcelize
class ProductPriceModel(
    @get:PropertyName("country_id")
    @set:PropertyName("country_id")
    var countryId : String? = null,
    var price: Float? = null,
) : Parcelable

enum class ProductSaleType( val type: String) {
    FLASH_SALE("flash_sale"),
    MEGA_SALE("mega_sale"),

}