package com.example.localbuddy

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.api.models.entity.Product
import com.example.localbuddy.data.Resource
import com.example.localbuddy.ui.auth.LoginFragment
import com.example.localbuddy.ui.auth.SignupFragment
import com.example.localbuddy.ui.home.ProductsAdapter
import com.google.android.material.snackbar.Snackbar

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.snackbar(
    message: String,
    action: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiCall(
    faliure: Resource.Faliure,
    retry: (() -> Unit)? = null
) {
    when {
        faliure.isNetworkError -> requireView().snackbar(
            "Please check your internet connection",
            retry
        )
        faliure.errorCode == 400 -> {
            if (this is LoginFragment) {
                requireView().snackbar("Incorrect email or password, Please try again!")
            }
            if (this is SignupFragment) {
                requireView().snackbar("Fill all the fields correctly!")
            }
        }
        faliure.errorCode == 401 -> {
            requireView().snackbar("Session timed out, Please try again!")
        }
        else -> {
            val error = faliure.errorBody.toString()
            requireView().snackbar(error)
        }
    }
}


fun ImageView.imgUrl(
    imgUrl: String?
) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        load(imgUri)
    }
}

fun RecyclerView.listData(
    data: List<Product>?
){
    val adapter = this.adapter as ProductsAdapter
    adapter.submitList(data)
}

