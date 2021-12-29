package com.example.api.models.entity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Feedback(
    @Json(name = "_id")
    val id: String,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "userAvatar")
    val userAvatar: String,
    @Json(name = "feedback")
    val feedback: String,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "timestamp")
    val timestamp: String,
    @Json(name = "updatedAt")
    val updatedAt: String,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "feedbackImg")
    val feedbackImg: String?
)