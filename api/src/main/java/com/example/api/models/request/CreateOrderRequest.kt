package com.example.api.models.request


import com.example.api.models.entity.CartItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateOrderRequest(
    @Json(name = "amount")
    val amount: Int,
    @Json(name = "cart")
    val cart: List<CartItem>,
    @Json(name = "userId")
    val userId: String
)