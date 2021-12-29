package com.example.localbuddy.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entity.Feedback
import com.example.api.models.entity.Product
import com.example.localbuddy.data.ProductsRepo
import com.example.localbuddy.data.Resource
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private var _product = MutableLiveData<Resource<Product>>()
    val product: LiveData<Resource<Product>> get() = _product

    private var _feedbacks = MutableLiveData<Resource<List<Feedback>>>()
    val feedbacks: LiveData<Resource<List<Feedback>>> get() = _feedbacks

    fun fetchProductById(productId: String) {
        viewModelScope.launch {
            try {
                ProductsRepo.getProductById(productId).let {
                    _product.value = it
                }
            } catch (e: Exception) {
                Log.d("ProductViewModel", e.message!!)
            }
        }
    }

    fun fetchFeedbackById(productId: String) {
        viewModelScope.launch {
            try {
                ProductsRepo.getFeedbacksById(productId).let {
                    _feedbacks.value = it
                }
            } catch (e: Exception) {
                Log.d("ProductViewModel", "${e.message}")
            }
        }
    }

    fun addFeedback(
        userId: String,
        username: String,
        userAvatar: String,
        feedback: String,
        rating: Int,
        productId: String,
        feedbackImg: String?
    ) {
        viewModelScope.launch {
            try {
                ProductsRepo.addFeedback(userId, username, userAvatar, productId, feedback, rating,feedbackImg)
            } catch (e: Exception) {
                Log.d("OrdersViewModel", "${e.message}")
            }
        }
    }

    fun deleteFeedback(id: String) {
        viewModelScope.launch {
            try {
                ProductsRepo.deleteFeedback(id)
            } catch (e: Exception) {
                Log.d("OrdersViewModel", "${e.message}")
            }
        }
    }
}