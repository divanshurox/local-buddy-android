package com.example.api.models.request


import com.example.api.models.entity.Photo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddProductRequest(
    @Json(name = "description")
    val description: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "photos")
    val photos: List<Photo>,
    @Json(name = "price")
    val price: Int,
    @Json(name = "sellerId")
    val sellerId: String
)