package com.example.localbuddy.data

import com.example.api.LocalBuddyClient
import com.example.api.models.entity.CartItem
import com.example.api.models.request.CreateOrderRequest
import com.example.api.services.LocalBuddyAuthAPI

object OrdersRepo {
    private val api: LocalBuddyAuthAPI = LocalBuddyClient.authApi

    suspend fun createOrder(
        amount: Int,
        userId: String,
        cart: List<CartItem>
    ) = BaseRepo.safeApiCall {
        api.createOrder(
            CreateOrderRequest(amount, cart, userId)
        )
    }
}