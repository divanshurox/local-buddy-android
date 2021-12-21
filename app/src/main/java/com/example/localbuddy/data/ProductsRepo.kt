package com.example.localbuddy.data

import com.example.api.LocalBuddyClient
import com.example.api.models.request.AddFeedbackRequest
import com.example.api.models.request.DeleteFeedbackRequest
import com.example.api.models.request.GetFeedbacksRequest
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

    suspend fun addFeedback(
        userId: String,
        username: String,
        userAvatar: String,
        productId: String,
        feedback: String,
        rating: Int
    ) = BaseRepo.safeApiCall {
        api.addFeedback(
            AddFeedbackRequest(feedback, rating, userId, username, userAvatar, productId)
        )
    }

    suspend fun deleteFeedback(id: String) = BaseRepo.safeApiCall {
        api.deleteFeedback(
            DeleteFeedbackRequest(id)
        )
    }

    suspend fun getFeedbacksById(productId: String) = BaseRepo.safeApiCall {
        api.getFeedbacksById(
            GetFeedbacksRequest(productId)
        )
    }
}