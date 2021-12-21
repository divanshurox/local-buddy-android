package com.example.localbuddy

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.api.models.entity.Feedback
import com.example.api.models.entity.Order
import com.example.api.models.entity.Product
import com.example.localbuddy.data.Resource
import com.example.localbuddy.ui.auth.LoginFragment
import com.example.localbuddy.ui.auth.SignupFragment
import com.example.localbuddy.ui.home.ProductsAdapter
import com.example.localbuddy.ui.orders.OrdersListAdapter
import com.example.localbuddy.ui.product.FeedbacksListAdapter
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

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

fun Fragment.displayError(msg: String){
    requireView().snackbar(msg)
}


fun ImageView.imgUrl(
    imgUrl: String?
) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        load(imgUri)
    }
}

fun ImageView.avatarUrl(
    imgUrl: String?
) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        load(imgUri){
            transformations(CircleCropTransformation())
        }
    }
}

fun RecyclerView.listData(
    data: List<Product>?
){
    val adapter = this.adapter as ProductsAdapter
    adapter.submitList(data)
}

fun RecyclerView.listOrderData(
    data: List<Order>?
){
    val adapter = this.adapter as OrdersListAdapter
    adapter.submitList(data)
}

fun RecyclerView.listFeedbackData(
    data: List<Feedback>?
){
    val adapter = this.adapter as FeedbacksListAdapter
    adapter.submitList(data)
}

fun TextView.convertToDate(timestamp: String){
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = formatter.parse(timestamp)
    calendar.time = date
    val dateFormat = listOf(calendar.get(Calendar.DATE).toString(),calendar.get(Calendar.MONTH).toString(),calendar.get(Calendar.YEAR).toString())
    this.text = "Posted: ${dateFormat.joinToString("-")}"
}


