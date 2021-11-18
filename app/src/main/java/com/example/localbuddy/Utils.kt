package com.example.localbuddy

import android.view.View
import androidx.fragment.app.Fragment
import com.example.localbuddy.data.Resource
import com.example.localbuddy.ui.auth.LoginFragment
import com.example.localbuddy.ui.auth.SignupFragment
import com.google.android.material.snackbar.Snackbar

fun View.visible(isVisible: Boolean){
    visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.snackbar(
    message: String,
    action: (() -> Unit)? = null
){
    val snackbar = Snackbar.make(this,message,Snackbar.LENGTH_LONG)
    action?.let{
        snackbar.setAction("Retry"){
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiCall(
    faliure: Resource.Faliure,
    retry: (() -> Unit)? = null
){
    when{
        faliure.isNetworkError -> requireView().snackbar("Please check your internet connection",retry)
        faliure.errorCode == 400 -> {
            if(this is LoginFragment){
                requireView().snackbar("Incorrect email or password, Please try again")
            }
            if(this is SignupFragment){
                requireView().snackbar("Fill all the fields correctly!")
            }
        }
        else -> {
            val error = faliure.errorBody.toString()
            requireView().snackbar(error)
        }
    }
}