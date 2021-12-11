package com.example.api.services

import com.example.api.models.entity.Order
import com.example.api.models.entity.Product
import com.example.api.models.request.CreateOrderRequest
import com.example.api.models.request.ProductByIdRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LocalBuddyAuthAPI {
    @GET("/products")
    suspend fun getProductsList(): List<Product>

    @POST("/products/id")
    suspend fun getProductById(
        @Body productParams: ProductByIdRequest
    ): Product

    @POST("orders/")
    suspend fun createOrder(
        @Body cartParams: CreateOrderRequest
    ): Order

}