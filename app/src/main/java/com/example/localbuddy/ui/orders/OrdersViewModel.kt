package com.example.localbuddy.ui.orders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entity.Order
import com.example.localbuddy.data.OrdersRepo
import com.example.localbuddy.data.Resource
import kotlinx.coroutines.launch

class OrdersViewModel: ViewModel() {
    private val _orders = MutableLiveData<Resource<List<Order>>>()
    val orders: LiveData<Resource<List<Order>>> get() = _orders

    fun getOrders(userId: String){
        try{
            viewModelScope.launch{
                OrdersRepo.getOrders(userId).let{
                    _orders.value = it
                }
            }
        }catch (e: Exception){
            Log.d("OrdersViewModel", "${e.message}")
        }
    }
}