package com.example.localbuddy.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.localbuddy.R
import com.example.localbuddy.databinding.OrderConfirmBinding

class OrderConfirmed : Fragment() {
    private var _binding: OrderConfirmBinding? = null
    val binding: OrderConfirmBinding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OrderConfirmBinding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.clear()
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Order Placed"
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        binding?.apply {
            homeButton.setOnClickListener {
                val action = OrderConfirmedDirections.actionOrderConfirmedToNavHome()
                findNavController().navigate(action)
            }
        }
    }
}