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
import com.example.localbuddy.*
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentBuynowBinding
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
                    progressBar.visible(true)
                    placeOrder.visible(false)
                    cartViewModel.createOrder(
                        userId!!
                    )
                    val action = BuyNowFragmentDirections.actionBuyNowFragmentToOrderConfirmed()
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    displayError(e.message.toString())
                }
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