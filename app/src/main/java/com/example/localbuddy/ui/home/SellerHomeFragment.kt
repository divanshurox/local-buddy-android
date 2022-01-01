package com.example.localbuddy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localbuddy.*
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentSellerHomeBinding
import com.example.localbuddy.ui.product.AddStatus
import com.example.localbuddy.ui.product.ProductViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SellerHomeFragment : Fragment() {
    private var _binding: FragmentSellerHomeBinding? = null
    val binding: FragmentSellerHomeBinding get() = _binding!!

    private lateinit var productsAdapter: ProductsAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerHomeBinding.inflate(inflater, container, false)
        homeViewModel.fetchProductsBySellerId(
            (authViewModel.user.value as Resource.Success).value.id
        )
        productsAdapter = ProductsAdapter(true, {
            onDeleteProduct(it)
        }, {
            onClickProduct(it)
        })
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            productsList.layoutManager = LinearLayoutManager(context)
            productsList.adapter = productsAdapter
        }
        return binding.root
    }

    private fun onDeleteProduct(id: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Product")
            .setMessage("Once a product is deleted it cannot be reversed, are you sure?")
            .setNegativeButton("Cancel") { _, _ ->
                null
            }
            .setPositiveButton("Yes"){ _, _ ->
                productViewModel.deleteProduct(id)
            }
            .show()
        productViewModel.status.observe(viewLifecycleOwner){
            if(it == AddStatus.DONE){
                Toast.makeText(context,"Product deleted successfully!", Toast.LENGTH_SHORT).show()
            }else if(it == AddStatus.ERROR){
                displayError("Failed to delete product")
            }
        }
    }

    private fun onClickProduct(productId: String) {
        val bundle = bundleOf("productId" to productId)
        findNavController().navigate(R.id.action_nav_home_seller_to_productFragmentSeller, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Your Products"
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        homeViewModel.products.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> binding.apply {
                    productsList.listData(it.value)
                    swipeLayout.setOnRefreshListener {
                        homeViewModel.fetchProductsBySellerId(
                            (authViewModel.user.value as Resource.Success).value.id
                        )
                        swipeLayout.isRefreshing = false
                    }
                    authViewModel.user.value?.let {
                        when (it) {
                            is Resource.Success -> introMessage.text =
                                "Hey, ${it.value.firstname} ${it.value.lastname}"
                        }
                    }
                }
                is Resource.Faliure -> handleApiCall(it)
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}