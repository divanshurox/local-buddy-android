package com.example.api.models.entity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "url")
    val url: String
)