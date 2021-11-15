package com.example.api.models.entity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "name")
    val name: String,
    @Json(name = "photos")
    val photos: List<Photo>,
    @Json(name = "price")
    val price: Int,
    @Json(name = "sellerId")
    val sellerId: String
)