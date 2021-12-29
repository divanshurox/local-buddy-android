package com.example.localbuddy.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.localbuddy.AuthViewModel
import com.example.localbuddy.R
import com.example.localbuddy.databinding.FragmentSignupBinding
import com.example.localbuddy.visible

class SignupFragment: Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        authViewModel.user.observe(viewLifecycleOwner, {
//            Log.d("signup","inside onviewcreated")
//            when(it){
//                is Resource.Success -> {
//                    LocalBuddyClient.authToken = it.value.token
//                }
//                is Resource.Faliure -> handleApiCall(it)
//            }
//        })
        _binding?.apply{
            sellerSwitch.setOnCheckedChangeListener { _, isChecked ->
                gstShopnameLayout.visible(isChecked)
            }
            signupButton.setOnClickListener {
                authViewModel.saveUserDetails(
                    firstname.text.toString(),
                    lastname.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    phone.text.toString(),
                    address.text.toString(),
                    city.text.toString(),
                    state.text.toString(),
                    pincode.text.toString(),
                    username.text.toString(),
                    sellerSwitch.isChecked,
                    gstno.text.toString(),
                    shopname.text.toString()
                )
                val bundle = bundleOf("number" to phone.text.toString())
                findNavController().navigate(R.id.action_nav_signup_to_mobileAuthFragment,bundle)
//                authViewModel.user.observe(viewLifecycleOwner,{
//                    Log.d("signup","inside onviewcreated - 2")
//                    if(it is Resource.Success){
//                        val bundle = bundleOf("number" to phone.text.toString())
//                        findNavController().navigate(R.id.action_nav_signup_to_mobileAuthFragment,bundle)
//                    }
//                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}