package com.example.api.models.entity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "address")
    val address: Address,
    @Json(name = "email")
    val email: String,
    @Json(name = "firstname")
    val firstname: String,
    @Json(name = "gstno")
    val gstno: String,
    @Json(name = "_id")
    val id: String,
    @Json(name = "isSeller")
    val isSeller: Boolean,
    @Json(name = "lastname")
    val lastname: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "avatar")
    val avatar: String,
    @Json(name = "shopName")
    val shopName: String,
    @Json(name = "token")
    val token: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "__v")
    val v: Int
)