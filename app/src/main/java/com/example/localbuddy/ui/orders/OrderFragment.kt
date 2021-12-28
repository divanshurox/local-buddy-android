package com.example.localbuddy.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localbuddy.R
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentOrderBinding
import com.example.localbuddy.handleApiCall
import com.example.localbuddy.listOrderDetailData
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    val binding: FragmentOrderBinding get() = _binding!!
    private val orderViewModel: OrdersViewModel by activityViewModels()
    private val args by navArgs<OrderFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        _binding?.apply{
            lifecycleOwner = viewLifecycleOwner
            productList.adapter = OrderDetailsListAdapter()
            productList.layoutManager = LinearLayoutManager(context)
        }
        (activity as AppCompatActivity).supportActionBar?.apply{
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        orderViewModel.getOrderById(args.orderId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel.order.observe(viewLifecycleOwner) { order ->
            if (order is Resource.Success) {
                _binding?.apply {
                    productList.listOrderDetailData(order.value.cart)
                    orderIdText.text = "Order ID: ${order.value._id}"
                    razorpayIdText.text = "Razorpay ID: ${order.value.razorpayId}"
                    amount.text = "Rs. ${order.value.amount}"
                    totalAmount.text = "Rs. ${order.value.amount}"
                    deliveryDate.text = order.value.orderDate
                    cancelOrder.setOnClickListener {
                        val customLayout = layoutInflater.inflate(R.layout.cancel_order_dialog,null)
                        MaterialAlertDialogBuilder(requireContext())
                            .setView(customLayout)
                            .setTitle("Why are you cancelling the order?")
                            .setPositiveButton("Submit"){_,_ ->
                                orderViewModel.deleteOrderById(order.value._id).let{
                                    findNavController().navigate(R.id.action_orderFragment_to_nav_home)
                                }
                            }.show()
                    }
                }
            } else if (order is Resource.Faliure) {
                handleApiCall(order)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}