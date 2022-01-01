package com.example.localbuddy.ui.product

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localbuddy.*
import com.example.localbuddy.data.Resource
import com.example.localbuddy.databinding.FragmentProductBinding
import com.example.localbuddy.navObject.Product
import com.example.localbuddy.ui.checkout.CartViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import kotlin.properties.Delegates

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    val binding: FragmentProductBinding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private var productId: String? = null
    private lateinit var userId: String
    private lateinit var username: String
    private lateinit var userAvatar: String

    private lateinit var customLayout: View
    private lateinit var addImg: MaterialButton
    private lateinit var feedbackText: TextInputEditText
    private var selectedRadio by Delegates.notNull<Int>()
    private var rating by Delegates.notNull<Int>()
    private lateinit var feedbackImage: ImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var filename: String
    private lateinit var path: String

    private val startImagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data!!
                Log.d("ProductFrag", uri.path.toString())
                feedbackImage.apply {
                    visible(true)
                    setImageURI(uri)
                }
                path = uri.path!!
                filename = path.split("/").last()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        customLayout = layoutInflater.inflate(R.layout.feedback_dialog, null)
        addImg =
            customLayout.findViewById<MaterialButton>(R.id.addImage)
        feedbackText =
            customLayout.findViewById<TextInputEditText>(R.id.feedbackEditText)
        selectedRadio =
            customLayout.findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId
        rating =
            customLayout.findViewById<RadioButton>(selectedRadio).text.toString()
                .toInt()
        feedbackImage = customLayout.findViewById<ImageView>(R.id.feedback_image)
        arguments?.let {
            productId = it.getString("productId").toString()
        }
        storage = Firebase.storage
        productId?.let {
            viewModel.fetchProductById(it)
            viewModel.fetchFeedbackById(it)
        }
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            feedbackList.layoutManager = LinearLayoutManager(context)
            authViewModel.user.value.let {
                if (it is Resource.Success) {
                    userId = it.value.id
                    username = it.value.username
                    userAvatar = it.value.avatar
                    feedbackList.adapter = FeedbacksListAdapter(it.value.id) { feedbackId ->
                        deleteFeedback(feedbackId)
                    }
                }
            }
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
                        productImage.imgUrl(it.value.photos[0].url)
                        productTitle.text = it.value.name
                        productDesc.text = it.value.description
                        productPrice.text = "MRP: ${it.value.price}"
                        addToCart.setOnClickListener { _ ->
                            cartViewModel.addItem(it.value)
                            Toast.makeText(context, "Item added to cart.", Toast.LENGTH_LONG).show()
                        }
                        addFeedback.setOnClickListener {

                            addImg.setOnClickListener {
                                ImagePicker.with(requireActivity())
                                    .createIntent { intent ->
                                        startImagePicker.launch(intent)
                                    }
                            }
                            MaterialAlertDialogBuilder(requireContext())
                                .setView(customLayout)
                                .setTitle("Add Feedback")
                                .setPositiveButton("Submit") { _, _ ->
                                    var downloadLink: String? = null
                                    if (filename.isNotEmpty() && path.isNotEmpty()) {
                                        downloadLink = uploadImage(filename, path)
                                    }
                                    addFeedback(
                                        userId,
                                        username,
                                        userAvatar,
                                        feedbackText.text.toString(),
                                        rating,
                                        productId!!,
                                        downloadLink
                                    )
                                    findNavController().navigateUp()
                                }
                                .show()
                        }
                        buyNow.setOnClickListener { _ ->
                            cartViewModel.addItem(it.value)
                            val product = Product(
                                it.value.id,
                                it.value.sellerId,
                                it.value.name,
                                it.value.price,
                                it.value.description,
                                it.value.photos[0].url
                            )
                            val bundle = bundleOf("product" to product)
                            findNavController().navigate(R.id.action_nav_product_to_buyNowFragment,bundle)
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

        viewModel.feedbacks.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.apply {
                        feedbackList.listFeedbackData(it.value)
                        progressBar.visible(false)
                        feedbackList.visible(true)
                    }
                }
                is Resource.Faliure -> handleApiCall(it)
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
            Toast.makeText(context, "Uploaded image successfully", Toast.LENGTH_LONG).show()
        }
        return link
    }

    private fun deleteFeedback(feedbackId: String) {
        viewModel.deleteFeedback(feedbackId)
        Toast.makeText(context, "Feedback deleted successfully!", Toast.LENGTH_LONG).show()
    }

    private fun addFeedback(
        userId: String,
        username: String,
        userAvatar: String,
        feedback: String,
        rating: Int,
        productId: String,
        feedbackImg: String?
    ) {
        viewModel.addFeedback(
            userId,
            username,
            userAvatar,
            feedback,
            rating,
            productId,
            feedbackImg
        )
        Toast.makeText(context, "Feedback added.", Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}