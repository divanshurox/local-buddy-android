package com.example.localbuddy.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entity.Product
import com.example.localbuddy.data.ProductsRepo
import com.example.localbuddy.data.Resource
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private var _product = MutableLiveData<Resource<Product>>()
    val product: LiveData<Resource<Product>> get() = _product

    fun fetchProductById(productId: String){
        viewModelScope.launch {
            try{
                ProductsRepo.getProductById(productId).let{
                    _product.value = it
                }
            }catch (e: Exception){
                Log.d("ProductViewModel",e.message!!)
            }
        }
    }
}