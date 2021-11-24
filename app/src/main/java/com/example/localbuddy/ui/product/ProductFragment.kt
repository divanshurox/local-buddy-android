package com.example.localbuddy.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.localbuddy.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    val binding: FragmentProductBinding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}