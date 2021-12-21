package com.example.api.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddFeedbackRequest(
    @Json(name = "feedback")
    val feedback: String,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "userAvatar")
    val userAvatar: String,
    @Json(name = "productId")
    val productId: String,
)