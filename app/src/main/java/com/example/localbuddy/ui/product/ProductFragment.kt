package com.example.localbuddy.ui.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        arguments?.let {
            productId = it.getString("productId").toString()
        }
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
        productId: String
    ) {
        viewModel.addFeedback(userId, username, userAvatar, feedback, rating, productId)
        Toast.makeText(context, "Feedback added.", Toast.LENGTH_LONG).show()
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
                        productPrice.text = "MRP: ${it.value.price.toString()}"
                        addToCart.setOnClickListener { _ ->
                            cartViewModel.addItem(it.value)
                            Toast.makeText(context, "Item added to cart.", Toast.LENGTH_LONG).show()
                        }
                        addFeedback.setOnClickListener {
                            val customLayout =
                                layoutInflater.inflate(R.layout.feedback_dialog, null)
                            MaterialAlertDialogBuilder(requireContext())
                                .setView(customLayout)
                                .setTitle("Add Feedback")
                                .setPositiveButton("Submit") { _, _ ->
                                    val feedbackText =
                                        customLayout.findViewById<TextInputEditText>(R.id.feedbackEditText)
                                    val selectedRadio =
                                        customLayout.findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId
                                    val rating =
                                        customLayout.findViewById<RadioButton>(selectedRadio).text.toString()
                                            .toInt()
                                    addFeedback(
                                        userId,
                                        username,
                                        userAvatar,
                                        feedbackText.text.toString(),
                                        rating,
                                        productId!!
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
                            val action =
                                ProductFragmentDirections.actionNavProductToBuyNowFragment(product)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}