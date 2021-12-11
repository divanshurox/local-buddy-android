package com.example.api.models.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "userId")
    val userId: String,
    @Json(name = "cart")
    val cart: List<CartItem>,
    @Json(name = "amount")
    val amount: Int
)