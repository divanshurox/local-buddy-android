package com.example.localbuddy.ui.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localbuddy.AuthViewModel
import com.example.localbuddy.R
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentCheckoutBinding
import com.example.localbuddy.listCartData
import com.example.localbuddy.visible

class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    val binding: FragmentCheckoutBinding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            cartList.adapter = CartListAdapter({
                cartViewModel.removeItem(it)
            },
                {
                    cartViewModel.incQuantity(it)
                },
                {
                    cartViewModel.decQuantity(it)
                })
            cartList.layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Your Cart"
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        authViewModel.user.value?.let {
            if (it is Resource.Success) {
                userId = it.value.id
            }
        }
        cartViewModel.cartItems.observe(viewLifecycleOwner) {
            when(it?.size){
                0 -> {
                    _binding?.apply{
                        emptyCartLayout.visible(true)
                        cartList.visible(false)
                        buyNowBtn.isEnabled = false
                    }
                }
                else -> {
                    _binding?.apply {
                        buyNowBtn.text = "Proceed to buy ${it?.size} items"
                        cartList.listCartData(it)
                        giftOpt.setOnCheckedChangeListener{ _, isChecked ->
                            if(isChecked){
                                cartViewModel.optGiftingOption(true)
                            }else{
                                cartViewModel.optGiftingOption(false)
                            }
                        }
                        buyNowBtn.setOnClickListener{
                            buyNowBtn.visible(false)
                            progressBar.visible(true)
                            cartViewModel.createOrder(
                                userId!!
                            )
                            val action = CheckoutFragmentDirections.actionNavCartToOrderConfirmed()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
        cartViewModel.totalAmount.observe(viewLifecycleOwner) {
            _binding?.apply {
                productPrice.text =
                    "Rs. $it"
                totalAmount.text =
                    "Rs. $it"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}