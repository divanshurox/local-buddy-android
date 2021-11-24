package com.example.localbuddy.data

import com.example.api.LocalBuddyClient
import com.example.api.models.entity.Address
import com.example.api.models.request.SigninRequest
import com.example.api.models.request.SigninTokenRequest
import com.example.api.models.request.SignupSellerRequest
import com.example.api.models.request.SignupUserRequest
import com.example.api.services.LocalBuddyAPI

object AuthRepo {
    private val api: LocalBuddyAPI = LocalBuddyClient.publicApi

    suspend fun login(username: String, password: String) = BaseRepo.safeApiCall {
        api.signinUser(
            SigninRequest(username, password)
        )
    }

    suspend fun registerUser(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        address: Address,
        username: String,
        isSeller: Boolean = false
    ) = BaseRepo.safeApiCall {
        api.registerUser(
            SignupUserRequest(
                firstname,
                lastname,
                email,
                password,
                phone,
                address,
                username,
                isSeller
            )
        )
    }

    suspend fun registerSeller(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        address: Address,
        username: String,
        gstno: String,
        shopname: String,
        isSeller: Boolean = true
    ) = BaseRepo.safeApiCall {
        api.registerSeller(
            SignupSellerRequest(
                firstname,
                lastname,
                email,
                password,
                phone,
                address,
                username,
                gstno,
                shopname,
                isSeller
            )
        )
    }

    suspend fun signinUserToken(token: String) = BaseRepo.safeApiCall {
        api.signinUserToken(
            SigninTokenRequest(token)
        )
    }
}