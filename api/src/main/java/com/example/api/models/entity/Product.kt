package com.example.api.models.entity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "sellerId")
    val sellerId: String,
    @Json(name = "photos")
    val photos: List<Photo>
)