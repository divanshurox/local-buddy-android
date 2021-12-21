package com.example.api.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetFeedbacksRequest(
    @Json(name = "productId")
    val productId: String
)