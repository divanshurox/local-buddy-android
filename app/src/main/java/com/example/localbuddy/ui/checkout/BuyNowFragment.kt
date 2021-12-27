package com.example.localbuddy.ui.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.localbuddy.*
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentBuynowBinding
import com.razorpay.Checkout
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class BuyNowFragment : Fragment() {
    private var _binding: FragmentBuynowBinding? = null
    val binding: FragmentBuynowBinding get() = _binding!!
    private val args by navArgs<BuyNowFragmentArgs>()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuynowBinding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Place order"
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        authViewModel.user.value?.let {
            if (it is Resource.Success) {
                userId = it.value.id
            }
        }
        binding.apply {
            productImage.imgUrl(args.product.photo)
            authViewModel.user.value?.let {
                if (it is Resource.Success) {
                    userInfo.text =
                        it.value.firstname + ", " + it.value.address.addressLine + ", " + it.value.address.city
                }
            }
            deliveryDate.text = getDeliveryDate()
            productPrice.text = args.product.price.toString()
            totalAmount.text = args.product.price.toString()
            placeOrder.setOnClickListener {
                try {
                    progressBar.visible(true)
                    placeOrder.visible(false)
                    cartViewModel.createOrder(
                        userId!!
                    )
                } catch (e: Exception) {
                    displayError(e.message.toString())
                }
            }
        }
        cartViewModel.orderDetails.observe(viewLifecycleOwner){
            if(it is Resource.Success){
                startPayment(it.value.amount,it.value.razorpayId)
            }
        }
        cartViewModel.orderStatus.observe(viewLifecycleOwner){
            if(it == OrderStatus.SUCCESS){
                Toast.makeText(context,"Payment successful!",Toast.LENGTH_LONG).show()
                val action = BuyNowFragmentDirections.actionBuyNowFragmentToOrderConfirmed()
                findNavController().navigate(action)
            }else if(it == OrderStatus.ERROR){
                displayError("Payment failed")
            }
        }
    }

    private fun startPayment(amount: Int,orderId: String){
        val co = Checkout()
        co.setKeyID("rzp_test_HreYOPYxd0zZw6")
        try{
            val options = JSONObject()
            options.put("name","LocalBuddy")
            options.put("description","Empowering local stores")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://firebasestorage.googleapis.com/v0/b/localbuddy-da7be.appspot.com/o/store.png?alt=media&token=176ccc37-c9f7-4b5d-92f6-52bbafdb51de")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("order_id", orderId);
            options.put("amount",amount)//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email",(authViewModel.user.value as Resource.Success).value.email)
            prefill.put("contact",(authViewModel.user.value as Resource.Success).value.phone)
            options.put("prefill",prefill)
            co.open(activity,options)
        }catch(e: Exception){
            Toast.makeText(context,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun getDeliveryDate(): String {
        val calender = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        calender.add(Calendar.DATE, 7)
        val date = formatter.format(calender.time)
        date.split(" ").joinToString("-")
        return date
    }
}