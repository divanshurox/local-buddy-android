package com.example.localbuddy.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.localbuddy.AuthViewModel
import com.example.localbuddy.R
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentBuynowBinding
import com.example.localbuddy.imgUrl
import java.text.SimpleDateFormat
import java.util.*

class BuyNowFragment : Fragment() {
    private var _binding: FragmentBuynowBinding? = null
    val binding: FragmentBuynowBinding get() = _binding!!
    private val args by navArgs<BuyNowFragmentArgs>()
    private val authViewModel: AuthViewModel by activityViewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Place order"
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        binding?.apply {
            productImage.imgUrl(args.product.photo)
            authViewModel.user.value?.let {
                if (it is Resource.Success) {
                    userInfo.text =
                        it.value.firstname + ", " + it.value.address.addressLine + ", " + it.value.address.city
                }
            }
            deliveryDate.text = getDeliveryDate()
            placeOrder.setOnClickListener{
                val action = BuyNowFragmentDirections.actionBuyNowFragmentToOrderConfirmed()
                findNavController().navigate(action)
            }
        }
    }

    private fun getDeliveryDate(): String {
        val calender = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        calender.add(Calendar.DATE, 7)
        return formatter.format(calender.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}