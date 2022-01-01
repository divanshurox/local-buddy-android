package com.example.localbuddy.ui.product

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.localbuddy.AuthViewModel
import com.example.localbuddy.R
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentAddProductBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream

class AddProductFragment: Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    val binding: FragmentAddProductBinding get() = _binding!!
    private lateinit var storage: FirebaseStorage
    private var filename: String? = null
    private var path: String? = null
    private val productViewModel: ProductViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private val startImagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data!!
                Log.d("ProductFrag", uri.path.toString())
                _binding?.productImage?.setImageURI(uri)
                path = uri.path!!
                filename = path!!.split("/").last()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater,container,false)
        storage = Firebase.storage
        _binding?.apply{
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply{
            floatingActionButton.setOnClickListener{
                ImagePicker.with(requireActivity())
                    .createIntent { intent ->
                        startImagePicker.launch(intent)
                    }
            }
            productName.doOnTextChanged{ text, _, _, _ ->
                if(text!!.isNotEmpty()){
                    productNameLayout.error = null
                }
            }
            productPrice.doOnTextChanged{ text, _, _, _ ->
                if(text!!.isNotEmpty()){
                    productPriceLayout.error = null
                }
            }
            productDesc.doOnTextChanged{ text, _, _, _ ->
                if(text!!.isNotEmpty()){
                    productDescLayout.error = null
                }
            }
            addProductBtn.setOnClickListener{
                if(productName.text.toString().isEmpty()){
                    productNameLayout.error = "Add product name"
                    return@setOnClickListener
                }
                if(productPrice.text.toString().isEmpty()){
                    productPriceLayout.error = "Add product price"
                    return@setOnClickListener
                }
                if(productDesc.text.toString().isEmpty()){
                    productDescLayout.error = "Add product description"
                    return@setOnClickListener
                }
                if(productDesc.text.toString().length < 5){
                    productDescLayout.error = "Product description must be longer"
                    return@setOnClickListener
                }
                var downloadLink = "https://www.arraymedical.com/wp-content/uploads/2018/12/product-image-placeholder.jpg"
                if (!filename.isNullOrEmpty() && !path.isNullOrEmpty()) {
                    downloadLink = uploadImage(filename!!, path!!)!!
                }
                productViewModel.addProduct(
                    (authViewModel.user.value as Resource.Success).value.id,
                    productName.text.toString(),
                    productDesc.text.toString(),
                    productPrice.text.toString().toInt(),
                    downloadLink
                )
                productViewModel.status.observe(viewLifecycleOwner){
                    if(it == AddStatus.DONE){
                        Toast.makeText(context,"Product added successfully!",Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_nav_add_item_to_nav_home_seller)
                    }
                }
            }
        }
    }

    private fun uploadImage(filename: String, path: String): String? {
        val storageRef = storage.reference
        val imageRef = storageRef.child(filename)
        val stream = FileInputStream(File(path))
        val uploadTask = imageRef.putStream(stream)
        var link: String? = null
        uploadTask.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                link = it.toString()
            }
        }
        return link
    }
}