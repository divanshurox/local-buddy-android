package com.example.localbuddy.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.localbuddy.R
import com.example.localbuddy.databinding.FragmentAuthmobileBinding
import com.example.localbuddy.displayError
import com.example.localbuddy.visible
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MobileAuthFragment : Fragment() {
    private var _binding: FragmentAuthmobileBinding? = null
    private val binding get() = _binding!!
    private var number: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = FragmentAuthmobileBinding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        arguments?.let {
            number = it.getString("number").toString()
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                binding.loadingIndicator.visible(false)
                binding.sendOtp.visible(true)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                binding.loadingIndicator.visible(false)
                binding.sendOtp.visible(true)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    displayError("Invalid Request")
                } else if (e is FirebaseTooManyRequestsException) {
                    displayError("The SMS quota for the project has been exceeded")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                binding.loadingIndicator.visible(false)
                binding.sendOtp.visible(true)
                val bundle = bundleOf("number" to number, "backendOtp" to verificationId)
                findNavController().navigate(
                    R.id.action_mobileAuthFragment_to_otpValidationFragment,
                    bundle
                )
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            numberEditText.setText(number)
            sendOtp.setOnClickListener {
                loadingIndicator.visible(true)
                sendOtp(number.toString())
            }
        }
    }

    private fun sendOtp(number: String){
        if (number.trim().isNotEmpty() && number.toString().trim().length == 10) {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91" + number!!)
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity!!)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
