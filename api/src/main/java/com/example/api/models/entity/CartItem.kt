package com.example.api.models.entity

import com.squareup.moshi.Json

data class CartItem(
    @Json(name = "product")
    val product: Product,
    @Json(name = "quantity")
    val quantity: Int
)