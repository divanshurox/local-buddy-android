package com.example.api.models.entity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "addressLine")
    val addressLine: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "pincode")
    val pincode: String,
    @Json(name = "state")
    val state: String
)