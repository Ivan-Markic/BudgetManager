package hr.markic.budgetmanager.api

import com.google.gson.annotations.SerializedName

data class ProductItem(

    @SerializedName("title") val title : String,
    @SerializedName("price") val price : Double,
    @SerializedName("description") val description : String,
    @SerializedName("category") val category : String,
    @SerializedName("image") val imageUrl : String


)
