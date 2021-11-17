package com.example.localbuddy.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.localbuddy.AuthViewModel
import com.example.localbuddy.R
import com.example.localbuddy.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _binding?.apply {
            viewModel = authViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            signupText.setOnClickListener {
                findNavController().navigate(R.id.action_nav_login_to_nav_signup)
            }
            loginButton.setOnClickListener {
                authViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                if(!authViewModel.error.value.isNullOrEmpty()){
                    Log.d("LoginFragment","yes")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}