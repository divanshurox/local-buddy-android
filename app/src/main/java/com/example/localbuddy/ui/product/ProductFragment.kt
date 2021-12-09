package com.example.localbuddy.ui.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.localbuddy.R
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentProductBinding
import com.example.localbuddy.handleApiCall
import com.example.localbuddy.imgUrl
import com.example.localbuddy.navObject.Product
import com.example.localbuddy.ui.checkout.CartViewModel

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    val binding: FragmentProductBinding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private var productId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        arguments?.let{
            productId = it.getString("productId").toString()
        }
        Log.d("ProductFragment",productId!!)
        productId?.let{
            viewModel.fetchProductById(it)
        }
        _binding?.apply{
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.product.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Success -> {
                    binding.apply{
                        productImage.imgUrl(it.value.photos[0].url)
                        productTitle.text = it.value.name
                        productDesc.text = it.value.description
                        productPrice.text = "MRP: ${it.value.price.toString()}"
                         addToCart.setOnClickListener { _ -> cartViewModel.addItem(it.value) }
                         buyNow.setOnClickListener { _ ->
                             val product = Product(
                                 it.value.id,
                                 it.value.sellerId,
                                 it.value.name,
                                 it.value.price,
                                 it.value.description,
                                 it.value.photos[0].url
                             )
                             val action = ProductFragmentDirections.actionNavProductToBuyNowFragment(product)
                             findNavController().navigate(action)
                         }
                    }
                    (activity as AppCompatActivity).supportActionBar?.apply {
                        title = it.value.name
                        setHomeAsUpIndicator(R.drawable.ic_menu)
                    }
                }
                is Resource.Faliure -> handleApiCall(it)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}