package com.example.localbuddy.ui.product

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
import com.example.localbuddy.*
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentProductsellerBinding
import com.example.localbuddy.navObject.Product

class ProductFragmentSeller : Fragment() {
    private var _binding: FragmentProductsellerBinding? = null
    val binding: FragmentProductsellerBinding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private var productId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsellerBinding.inflate(inflater, container, false)
        arguments?.let {
            productId = it.getString("productId").toString()
        }
        productId?.let {
            viewModel.fetchProductById(it)
            viewModel.fetchFeedbackById(it)
        }
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.product.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.apply {
                        floatingActionButton.apply{
                            visible((authViewModel.user.value as Resource.Success).value.isSeller)
                            val product = Product(
                                it.value.id,
                                it.value.sellerId,
                                it.value.name,
                                it.value.price,
                                it.value.description,
                                it.value.photos[0].url
                            )
                            setOnClickListener{
                                val action = ProductFragmentSellerDirections.actionProductFragmentSellerToEditProductFragment(product)
                                findNavController().navigate(action)
                            }
                        }
                        productImage.imgUrl(it.value.photos[0].url)
                        productTitle.text = it.value.name
                        productDesc.text = it.value.description
                        productPrice.text = "MRP: ${it.value.price}"
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