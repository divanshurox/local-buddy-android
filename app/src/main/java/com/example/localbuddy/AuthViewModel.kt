package com.example.localbuddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entity.Address
import com.example.api.models.entity.User
import com.example.localbuddy.data.AuthRepo
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    companion object {
        const val classTag: String = "AuthViewModel"
    }

    private var _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private var _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                AuthRepo.login(username, password)?.let {
                    _user.value = it
                    Log.d(classTag, "something")
                }
            } catch (e: Exception) {
                Log.d(classTag, "${e.message}")
            }
        }
    }

    fun registerUser(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        address: String,
        city: String,
        state: String,
        pincode: String,
        username: String,
        isSeller: Boolean
    ) {
        viewModelScope.launch {
            try {
                AuthRepo.registerUser(
                    firstname,
                    lastname,
                    email,
                    password,
                    phone,
                    Address(address, city, state, pincode),
                    username,
                    isSeller
                )?.let {
                    _user.value = it
                }
            } catch (e: Exception) {
                Log.d(classTag, "${e.message}")
            }
        }
    }

    fun registerSeller(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        address: String,
        city: String,
        state: String,
        pincode: String,
        username: String,
        isSeller: Boolean,
        gstno: String,
        shopname: String
    ) {
        viewModelScope.launch {
            try {
                AuthRepo.registerSeller(
                    firstname,
                    lastname,
                    email,
                    password,
                    phone,
                    Address(address, city, state, pincode),
                    username,
                    gstno,
                    shopname,
                    isSeller
                )?.let {
                    _user.value = it
                }
            } catch (e: Exception) {
                Log.d(classTag, "${e.message}")
            }
        }
    }
}