package com.example.localbuddy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localbuddy.*
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentHomeBinding
import com.example.localbuddy.ui.checkout.CartViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding get() = _binding!!

    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var homeViewModel: HomeViewModel
    private val authViewModel: AuthViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        productsAdapter = ProductsAdapter {
            onClickProduct(it)
        }
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            productsList.layoutManager = LinearLayoutManager(context)
            productsList.adapter = productsAdapter
        }
        return binding.root
    }

    private fun onClickProduct(productId: String){
        val bundle = bundleOf("productId" to productId)
        findNavController().navigate(R.id.action_homeFragment_to_productFragment,bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.products.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Success -> binding.apply{
                    productsList.listData(it.value)
                    fab.setOnClickListener {
                        val action = HomeFragmentDirections.actionNavHomeToNavCart()
                        findNavController().navigate(action)
                    }
                    authViewModel.user.value?.let{
                        when(it){
                            is Resource.Success -> introMessage.text = "Hey, ${it.value.firstname} ${it.value.lastname}"
                        }
                    }

                }
                is Resource.Faliure -> handleApiCall(it)
            }
        })
        cartViewModel.cartItems.observe(viewLifecycleOwner){
            _binding?.apply{
                if(it!!.size>0){
                    fab.visible(true)
                }else{
                    fab.visible(false)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}