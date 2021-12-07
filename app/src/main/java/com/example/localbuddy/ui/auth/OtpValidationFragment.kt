package com.example.localbuddy.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.localbuddy.R
import com.example.localbuddy.databinding.FragmentValidateotpBinding
import com.example.localbuddy.displayError
import com.example.localbuddy.visible
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OtpValidationFragment : Fragment() {
    private var _binding: FragmentValidateotpBinding? = null
    private val binding get() = _binding!!
    private lateinit var backendOtp: String
    private lateinit var number: String
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = FragmentValidateotpBinding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        arguments?.let {
            number = it.getString("number").toString()
            backendOtp = it.getString("backendOtp").toString()
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                binding.loadingIndicator.visible(false)
                binding.verifyOtp.visible(true)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                binding.loadingIndicator.visible(false)
                binding.verifyOtp.visible(true)
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
                binding.verifyOtp.visible(true)
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
            verifyOtp.setOnClickListener {
                val userOtp = opt1.text.toString() +
                        opt2.text.toString() +
                        opt3.text.toString() +
                        opt4.text.toString() +
                        opt5.text.toString() +
                        opt6.text.toString()
                loadingIndicator.visible(true)
                it.visible(false)
                val credential = PhoneAuthProvider.getCredential(backendOtp,userOtp)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(activity!!) {task ->
                        if(task.isSuccessful){
                            val navController = findNavController()
                            val inflater = navController.navInflater
                            navController.popBackStack()
                            navController.graph = inflater.inflate(R.navigation.nav_graph_auth)
                        }else{
                            displayError("Wrong OTP! Please try again.")
                        }
                    }
            }
            resendOtp.setOnClickListener {
                sendOtp(number)
            }
            opt1.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        opt2.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            opt2.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        opt3.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            opt3.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        opt4.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            opt4.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        opt5.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            opt5.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().trim().isEmpty()){
                        opt6.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
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
    }
}