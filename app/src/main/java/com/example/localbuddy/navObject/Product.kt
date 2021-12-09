package com.example.localbuddy.navObject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val sellerId: String,
    val name: String,
    val price: Int,
    val description: String,
    val photo: String
): Parcelable