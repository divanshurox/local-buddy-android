package com.example.localbuddy.data

import com.example.api.LocalBuddyClient
import com.example.api.models.request.ProductByIdRequest
import com.example.api.services.LocalBuddyAuthAPI

object ProductsRepo {
    private val api: LocalBuddyAuthAPI = LocalBuddyClient.authApi

    suspend fun getProductsList() = BaseRepo.safeApiCall { api.getProductsList() }

    suspend fun getProductById(productId: String) = BaseRepo.safeApiCall {
        api.getProductById(
            ProductByIdRequest(productId)
        )
    }
}