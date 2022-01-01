package com.example.api.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProductRequest(
    @Json(name = "description")
    val description: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "id")
    val id: String
)