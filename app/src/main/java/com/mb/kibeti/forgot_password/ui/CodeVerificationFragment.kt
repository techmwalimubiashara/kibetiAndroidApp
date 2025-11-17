package com.mb.kibeti.forgot_password.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mb.kibeti.R
import com.mb.kibeti.databinding.FragmentCodeVerificationBinding
import com.mb.kibeti.forgot_password.data.ForgotPasswordViewModel
import com.mb.kibeti.forgot_password.data.ForgotPasswordViewModelFactory
import com.mb.kibeti.forgot_password.data.ForgotPasswordRepository
import com.mb.kibeti.forgot_password.RetrofitInstance
import com.mb.kibeti.forgot_password.utils.Resource
import com.mb.kibeti.forgot_password.utils.SharedPreferencesHelper
import com.mb.kibeti.forgot_password.utils.TimerUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CodeVerificationFragment : Fragment(R.layout.fragment_code_verification) {

    private var _binding: FragmentCodeVerificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var countDownTimer: CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCodeVerificationBinding.bind(view)

        setupViewModel()
        setupUI()
        observeViewModel()
        startCountdownTimer()
        setupBackNavigation()
        displayCustomerEmail()
        setupOtpFields()
    }

    private fun setupBackNavigation() {
        binding.backButton.setOnClickListener { findNavController().navigate(R.id.action_codeVerificationFragment_to_emailFragment) }
        binding.backText.setOnClickListener { findNavController().navigate(R.id.action_codeVerificationFragment_to_emailFragment) }
    }

    private fun displayCustomerEmail() {
        val email = SharedPreferencesHelper.getEmail(requireContext()) ?: "unknown@example.com"
        val maskedEmail = maskEmail(email)
        binding.instructionText.text = "We have sent an OTP code to your email $maskedEmail."
    }

    private fun maskEmail(email: String): String {
        val atIndex = email.indexOf('@')
        return if (atIndex > 2) {
            email.replaceRange(2, atIndex, "*".repeat(atIndex - 2))
        } else {
            email
        }
    }

    private fun setupOtpFields() {
        val otpFields = listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4)

        otpFields.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < otpFields.size - 1) {
                        otpFields[index + 1].requestFocus()
                    } else if (s.isNullOrEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL &&
                    event.action == android.view.KeyEvent.ACTION_DOWN &&
                    editText.text.isEmpty() &&
                    index > 0
                ) {
                    otpFields[index - 1].requestFocus()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupViewModel() {
        val apiService = RetrofitInstance.api
        val repository = ForgotPasswordRepository(apiService)
        val factory = ForgotPasswordViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]
    }

    private fun setupUI() {
        binding.btnContinue.setOnClickListener { handleOtpVerification() }
        binding.tvResendCode.setOnClickListener { resendOtp() }
    }

    private fun handleOtpVerification() {
        val otp = binding.otp1.text.toString() +
                binding.otp2.text.toString() +
                binding.otp3.text.toString() +
                binding.otp4.text.toString()

        if (otp.length == 4) {
            verifyResetCode(otp)
        } else {
            showErrorMessage("Please enter a valid 4-digit OTP.")
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resetCodeVerificationResult.collectLatest { result ->
                    handleVerificationResult(result)
                }
            }
        }
    }

    private fun handleVerificationResult(result: Resource<String>) {
        when (result) {
            is Resource.Loading -> showLoading(true)
            is Resource.Success -> navigateToResetPassword()
            is Resource.Error -> showErrorMessage(result.message ?: "An unknown error occurred")
            else -> showLoading(false)
        }
    }

    private fun verifyResetCode(inputCode: String) {
        val savedCode = SharedPreferencesHelper.getResetCode(requireContext())
        Log.d("CodeVerification", "Saved Code: $savedCode, Input Code: $inputCode")

        if (inputCode == savedCode) {
            SharedPreferencesHelper.clearResetCode(requireContext())
            navigateToResetPassword()
        } else {
            showErrorMessage("Invalid code. Please try again.")
        }
    }

    private fun resendOtp() {
        val newResetCode = generateResetCode()
        SharedPreferencesHelper.saveResetCode(requireContext(), newResetCode)

        val email = SharedPreferencesHelper.getEmail(requireContext())
        if (email != null) {
            viewModel.sendResetCode(email, newResetCode)
            showResendUi()
            startCountdownTimer()
            Toast.makeText(requireContext(), "A new reset code has been sent to your email.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error: Email not found. Please go back and enter your email again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateResetCode(): String = (1000..9999).random().toString()

    private fun startCountdownTimer() {
        countDownTimer = TimerUtils.createCountdownTimer(
            duration = 30000,
            interval = 1000,
            onTick = { millisUntilFinished ->
                binding.tvCountdownTimer.text = "You can resend code in ${millisUntilFinished / 1000} sec"
            },
            onFinish = {
                binding.tvCountdownTimer.visibility = View.GONE
                binding.tvResendCode.visibility = View.VISIBLE
            }
        )
        countDownTimer.start()
    }

    private fun navigateToResetPassword() {
        findNavController().navigate(R.id.action_codeVerificationFragment_to_resetPasswordFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(message: String) {
        binding.tvErrorMessage.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun showResendUi() {
        binding.tvResendCode.visibility = View.GONE
        binding.tvCountdownTimer.visibility = View.VISIBLE
        binding.tvCountdownTimer.text = "You can resend code in 30 sec"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}
