package com.example.localbuddy.data

import com.example.api.LocalBuddyClient
import com.example.api.models.entity.Photo
import com.example.api.models.request.*
import com.example.api.services.LocalBuddyAuthAPI

object ProductsRepo {
    private val api: LocalBuddyAuthAPI = LocalBuddyClient.authApi

    suspend fun getProductsList() = BaseRepo.safeApiCall { api.getProductsList() }

    suspend fun getProductsListBySellerId(sellerId: String) = BaseRepo.safeApiCall {
        api.getProductsListBySellerId(
            sellerId
        )
    }

    suspend fun addProduct(
        sellerId: String,
        name: String,
        description: String,
        price: Int,
        photo: String
    ) = BaseRepo.safeApiCall {
        api.addProduct(
            AddProductRequest(
                description,
                name,
                listOf(Photo(photo)),
                price,
                sellerId
            )
        )
    }

    suspend fun updateProduct(
        id: String,
        name: String,
        price: Int,
        description: String
    ) = BaseRepo.safeApiCall {
        api.updateProduct(
            UpdateProductRequest(
                description,
                name,
                price,
                id
            )
        )
    }

    suspend fun deleteProduct(
        id: String
    ) = BaseRepo.safeApiCall {
        api.deleteProduct(
            id
        )
    }

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
        rating: Int,
        feedbackImg: String?
    ) = BaseRepo.safeApiCall {
        api.addFeedback(
            AddFeedbackRequest(
                feedback,
                rating,
                userId,
                username,
                userAvatar,
                productId,
                feedbackImg
            )
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