package com.example.localbuddy.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entity.Product
import com.example.localbuddy.data.ProductsRepo
import com.example.localbuddy.data.Resource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    companion object {
        const val classTag: String = "HomeViewModel"
    }
    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> get() = _products


    fun fetchProducts() {
        viewModelScope.launch {
            try {
                ProductsRepo.getProductsList().let {
                    _products.value = it
                }
            } catch (e: Exception) {
                Log.d(classTag, "${e.message}")
            }
        }
    }

    fun fetchProductsBySellerId(sellerId: String) {
        viewModelScope.launch {
            try {
                ProductsRepo.getProductsListBySellerId(sellerId).let {
                    _products.value = it
                }
            } catch (e: Exception) {
                Log.d(classTag, "${e.message}")
            }
        }
    }
}