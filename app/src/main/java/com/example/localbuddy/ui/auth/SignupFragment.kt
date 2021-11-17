package com.example.localbuddy.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.localbuddy.AuthViewModel
import com.example.localbuddy.databinding.FragmentSignupBinding

class SignupFragment: Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

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
        _binding?.apply{
            signupButton.setOnClickListener {
                when(sellerSwitch.isChecked){
                    true -> {
                        authViewModel.registerSeller(
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
                            true,
                            gstno.text.toString(),
                            shopname.text.toString()
                        )
                    }
                    else -> {
                        authViewModel.registerUser(
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
                            false
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}