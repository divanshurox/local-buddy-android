package com.example.localbuddy.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.api.models.entity.Product

class CartViewModel: ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<Product>>()
    val cartItems: LiveData<MutableList<Product>> get() = _cartItems

    fun addItem(product: Product){
        _cartItems.value?.add(product)
    }

    fun removeItem(productId: String){
        _cartItems.value?.removeAll{ it.id == productId}
    }
}