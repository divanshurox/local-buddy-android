package com.example.localbuddy.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.localbuddy.R
import com.example.localbuddy.databinding.FragmentEditProductBinding
import com.example.localbuddy.displayError
import com.example.localbuddy.imgUrl

class EditProductFragment: Fragment() {
    private var _binding: FragmentEditProductBinding? = null
    val binding: FragmentEditProductBinding get() = _binding!!
    private val args by navArgs<EditProductFragmentArgs>()
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        (activity as AppCompatActivity).supportActionBar?.apply{
            title="Update Product"
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply{
            productImage.imgUrl(args.product.photo)
            nameEditText.setText(args.product.name)
            priceEditText.setText(args.product.price.toString())
            descEditText.setText(args.product.description)
            updateProduct.setOnClickListener {
                productViewModel.updateProduct(
                    args.product.id,
                    nameEditText.text.toString(),
                    priceEditText.text.toString().toInt(),
                    descEditText.text.toString()
                )
                productViewModel.status.observe(viewLifecycleOwner){
                    if(it == AddStatus.DONE){
                        Toast.makeText(context,"Product updated successfully!",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_editProductFragment_to_nav_home_seller)
                    }else if(it == AddStatus.ERROR){
                        displayError("Failed to update product")
                    }
                }
            }
        }
    }
}