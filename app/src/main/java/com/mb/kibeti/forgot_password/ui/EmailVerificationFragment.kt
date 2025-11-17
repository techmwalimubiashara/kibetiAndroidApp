package com.mb.kibeti.forgot_password.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mb.kibeti.R
import com.mb.kibeti.databinding.FragmentEmailVerificationBinding
import com.mb.kibeti.forgot_password.RetrofitInstance
import com.mb.kibeti.forgot_password.data.ForgotPasswordRepository
import com.mb.kibeti.forgot_password.data.ForgotPasswordViewModel
import com.mb.kibeti.forgot_password.data.ForgotPasswordViewModelFactory
import com.mb.kibeti.forgot_password.utils.SharedPreferencesHelper
import kotlin.random.Random

class EmailVerificationFragment : Fragment(R.layout.fragment_email_verification) {

    private lateinit var binding: FragmentEmailVerificationBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEmailVerificationBinding.bind(view)

        // Initialize repository and factory
        val apiService = RetrofitInstance.api
        val repository = ForgotPasswordRepository(apiService)
        val factory = ForgotPasswordViewModelFactory(repository)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]

        // Button click to check email and send reset code
        binding.btnSendEmail.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isNotEmpty()) {
                binding.tvErrorMessage.visibility = View.GONE // Hide previous error
                val resetCode = generateResetCode()
                saveResetCodeToPreferences(resetCode)
                saveEmailToPreferences(email)
                viewModel.sendResetCode(email, resetCode)
                Log.d("EmailVerificationFragment", "Reset code sent: $resetCode")
            } else {
                binding.tvErrorMessage.apply {
                    text = "Please enter a valid email"
                    visibility = View.VISIBLE
                }
            }
        }

        observeViewModel()
    }

    private fun saveResetCodeToPreferences(code: String) {
        SharedPreferencesHelper.saveResetCode(requireContext(), code)
    }

    private fun saveEmailToPreferences(email: String) {
        val sharedPreferences = requireContext().getSharedPreferences("ForgotPasswordPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("email", email).apply()
    }

    private fun observeViewModel() {
        viewModel.emailCheckResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = {
                    binding.tvErrorMessage.visibility = View.GONE
                    findNavController().navigate(R.id.action_emailFragment_to_codeVerificationFragment)
                },
                onFailure = {
                    binding.tvErrorMessage.apply {
                        text = "No account associated with this email"
                        visibility = View.VISIBLE
                    }
                }
            )
        }
    }

    private fun generateResetCode(): String {
        return Random.nextInt(1000, 9999).toString()
    }
}
