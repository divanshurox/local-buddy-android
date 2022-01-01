package com.example.api.services

import com.example.api.models.entity.Feedback
import com.example.api.models.entity.Order
import com.example.api.models.entity.Product
import com.example.api.models.request.*
import retrofit2.http.*

interface LocalBuddyAuthAPI {
    @GET("/products")
    suspend fun getProductsList(): List<Product>

    @GET("/products")
    suspend fun getProductsListBySellerId(
        @Query("sellerId") sellerId: String
    ): List<Product>

    @POST("/products/update")
    suspend fun updateProduct(
        @Body productParams: UpdateProductRequest
    )
    
    @POST("/products/delete/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String
    )

    @POST("/orders")
    suspend fun getOrdersList(
        @Body userCred: GetOrdersRequest
    ): List<Order>

    @POST("/products/id")
    suspend fun getProductById(
        @Body productParams: ProductByIdRequest
    ): Product

    @POST("/orders/create")
    suspend fun createOrder(
        @Body cartParams: CreateOrderRequest
    ): Order

    @GET("/orders/{id}")
    suspend fun getOrderById(
        @Path("id") id: String
    ): Order

    @POST("/orders/delete/{id}")
    suspend fun deleteOrderById(
        @Path("id") id: String
    ): Order

    @POST("/products/add-feedback")
    suspend fun addFeedback(
        @Body feedbackParams: AddFeedbackRequest
    ): Feedback

    @POST("/products/delete-feedback")
    suspend fun deleteFeedback(
        @Body feedbackParams: DeleteFeedbackRequest
    ): Feedback

    @POST("/products/feedback")
    suspend fun getFeedbacksById(
        @Body feedbackParams: GetFeedbacksRequest
    ): List<Feedback>

    @POST("/products/")
    suspend fun addProduct(
        @Body productParams: AddProductRequest
    ): Product

}