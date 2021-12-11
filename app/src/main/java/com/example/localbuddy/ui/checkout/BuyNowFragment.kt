package com.example.localbuddy.ui.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.api.models.entity.CartItem
import com.example.api.models.entity.Photo
import com.example.api.models.entity.Product
import com.example.localbuddy.AuthViewModel
import com.example.localbuddy.R
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentBuynowBinding
import com.example.localbuddy.displayError
import com.example.localbuddy.imgUrl
import java.text.SimpleDateFormat
import java.util.*

class BuyNowFragment : Fragment() {
    private var _binding: FragmentBuynowBinding? = null
    val binding: FragmentBuynowBinding get() = _binding!!
    private val args by navArgs<BuyNowFragmentArgs>()
    private val cartViewModel: CartViewModel by viewModels()
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
            placeOrder.setOnClickListener {
                try {
                    val product = Product(
                        args.product.id,
                        args.product.sellerId,
                        args.product.name,
                        args.product.price,
                        args.product.description,
                        listOf(Photo(args.product.photo))
                    )
                    cartViewModel.createOrder(
                        args.product.price,
                        listOf(CartItem(product, 1)),
                        userId!!
                    )
                } catch (e: Exception) {
                    displayError(e.message.toString())
                }
                val action = BuyNowFragmentDirections.actionBuyNowFragmentToOrderConfirmed()
                findNavController().navigate(action)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}