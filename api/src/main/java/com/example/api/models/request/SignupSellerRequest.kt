package com.example.api.models.request


import com.example.api.models.entity.Address
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupSellerRequest(
    @Json(name = "address")
    val address: Address,
    @Json(name = "email")
    val email: String,
    @Json(name = "firstname")
    val firstname: String,
    @Json(name = "gstno")
    val gstno: String,
    @Json(name = "isSeller")
    val isSeller: Boolean,
    @Json(name = "lastname")
    val lastname: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "shopname")
    val shopname: String,
    @Json(name = "username")
    val username: String
)