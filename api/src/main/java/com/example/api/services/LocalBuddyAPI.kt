package com.example.api.services

import com.example.api.models.entity.User
import com.example.api.models.request.SigninRequest
import com.example.api.models.request.SigninTokenRequest
import com.example.api.models.request.SignupSellerRequest
import com.example.api.models.request.SignupUserRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface LocalBuddyAPI {
    @POST("/auth/signup")
    suspend fun registerUser(
        @Body userCreds: SignupUserRequest
    ): User

    @POST("/auth/signup")
    suspend fun registerSeller(
        @Body userCreds: SignupSellerRequest
    ): User

    @POST("/auth/signin")
    suspend fun signinUser(
        @Body userCreds: SigninRequest
    ): User

    @POST("/auth/signin/token")
    suspend fun signinUserToken(
        @Body token: SigninTokenRequest
    ): User
}