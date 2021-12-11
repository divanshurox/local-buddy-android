package com.example.localbuddy.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entity.CartItem
import com.example.api.models.entity.Product
import com.example.localbuddy.data.OrdersRepo
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<CartItem>?>()
    val cartItems: LiveData<MutableList<CartItem>?> get() = _cartItems

    fun addItem(product: Product) {
        _cartItems.value?.map {
            if(it.product == product){
                it.quantity++
                return;
            }
        }
        _cartItems.value?.add(CartItem(product,1))
    }

    fun removeItem(productId: String) {
        _cartItems.value?.removeAll { it.product.id == productId }
    }

    fun createOrder(amount: Int,cart: List<CartItem>,userId: String){
        viewModelScope.launch{
            OrdersRepo.createOrder(amount,userId,cart)
        }
    }
}