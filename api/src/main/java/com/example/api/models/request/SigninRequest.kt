package com.example.api.models.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SigninRequest(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String
)