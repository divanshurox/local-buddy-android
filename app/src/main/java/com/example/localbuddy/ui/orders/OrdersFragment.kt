package com.example.localbuddy.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localbuddy.*
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    val binding: FragmentOrdersBinding get() = _binding!!
    private val orderViewModel: OrdersViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            ordersList.layoutManager = LinearLayoutManager(context)
            ordersList.adapter = OrdersListAdapter{
                onClick(it)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.user.value?.let {
            if (it is Resource.Success) {
                userId = it.value.id
            }
        }
        orderViewModel.getOrders(userId)
        orderViewModel.orders.observe(viewLifecycleOwner, {
            binding.progressBar.visible(false)
            when(it){
                is Resource.Success -> binding.apply{
                    ordersList.listOrderData(it.value)
                }
                is Resource.Faliure -> handleApiCall(it)
            }
        })
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    private fun onClick(orderId: String){
        val action = OrdersFragmentDirections.actionNavOrdersToOrderFragment(orderId)
        findNavController().navigate(action)
    }
}