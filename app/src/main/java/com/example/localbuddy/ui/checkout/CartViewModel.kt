package com.example.localbuddy.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entity.CartItem
import com.example.api.models.entity.Order
import com.example.api.models.entity.Product
import com.example.localbuddy.data.OrdersRepo
import com.example.localbuddy.data.Resource
import kotlinx.coroutines.launch

enum class OrderStatus{
    NONE, SUCCESS, ERROR
}

class CartViewModel : ViewModel() {
    companion object {
        val GIFTING = 500
    }
    private val _cartItems = MutableLiveData<MutableList<CartItem>?>()
    val cartItems: LiveData<MutableList<CartItem>?> get() = _cartItems

    private val _totalAmount = MutableLiveData<Int>()
    val totalAmount: LiveData<Int> get() = _totalAmount

    private val _orderDetails = MutableLiveData<Resource<Order>>()
    val orderDetails: LiveData<Resource<Order>> get() = _orderDetails

    private val _orderStatus = MutableLiveData<OrderStatus>()
    val orderStatus: LiveData<OrderStatus> get() = _orderStatus

    init{
        _cartItems.value = mutableListOf()
        _totalAmount.value = 0
        _orderStatus.value = OrderStatus.NONE
    }

    fun addItem(product: Product) {
        _cartItems.value?.map {
            if(it.product == product){
                it.quantity++
                _totalAmount.value = _totalAmount.value?.plus(product.price)
                return;
            }
        }
        _cartItems.value?.add(CartItem(product,1))
        _totalAmount.value = _totalAmount.value?.plus(product.price)
    }

    fun incQuantity(productId: String){
        _cartItems.value?.map{
            if(it.product.id==productId){
                it.quantity++
                _totalAmount.value = _totalAmount.value?.plus(it.product.price)
            }
        }
    }

    fun decQuantity(productId: String){
        _cartItems.value?.map{
            if(it.product.id==productId){
                it.quantity--
                _totalAmount.value = _totalAmount.value?.minus(it.product.price)
            }
        }
    }

    fun clear(){
        _cartItems.value = mutableListOf()
        _totalAmount.value = 0
        _orderStatus.value = OrderStatus.NONE
    }

    fun removeItem(productId: String) {
        val item = _cartItems.value?.find { it.product.id == productId }
        _cartItems.value?.removeAll { it.product.id == productId }
        _cartItems.postValue(_cartItems.value)
        _totalAmount.value = item?.product?.let { _totalAmount.value?.minus(it.price) }
    }

    fun optGiftingOption(yes: Boolean){
        if(yes){
            _totalAmount.value = _totalAmount.value?.plus(GIFTING)
        }else{
            _totalAmount.value = _totalAmount.value?.minus(GIFTING)
        }
    }

    fun createOrder(userId: String){
        if(_cartItems.value!!.size > 0){
            viewModelScope.launch{
                OrdersRepo.createOrder(_totalAmount.value!!,userId,_cartItems.value!!).let{
                    _orderDetails.value = it
                }
            }
        }
    }

    fun setStatus(status: String){
        when(status){
            "success" -> _orderStatus.value = OrderStatus.SUCCESS
            else -> _orderStatus.value = OrderStatus.ERROR
        }
    }

}