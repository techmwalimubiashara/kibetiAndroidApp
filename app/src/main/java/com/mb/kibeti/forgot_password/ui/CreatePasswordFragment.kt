package com.mb.kibeti.forgot_password.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mb.kibeti.R
import com.mb.kibeti.forgot_password.RetrofitInstance
import com.mb.kibeti.forgot_password.data.ForgotPasswordRepository
import com.mb.kibeti.forgot_password.data.ForgotPasswordViewModel
import com.mb.kibeti.forgot_password.data.ForgotPasswordViewModelFactory
import com.mb.kibeti.forgot_password.data.models.PasswordUiState
import com.mb.kibeti.forgot_password.utils.SharedPreferencesHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreatePasswordFragment : Fragment() {

    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var newPasswordToggle: ImageView
    private lateinit var confirmPasswordToggle: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var btnContinue: Button
    private lateinit var backButton: ImageView
    private lateinit var backText: TextView

    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_password, container, false)

        // Initialize views
        newPasswordInput = view.findViewById(R.id.newPasswordInput)
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput)
        newPasswordToggle = view.findViewById(R.id.newPasswordToggle)
        confirmPasswordToggle = view.findViewById(R.id.confirmPasswordToggle)
        errorMessage = view.findViewById(R.id.errorMessage)
        btnContinue = view.findViewById(R.id.btnContinue)
        backButton = view.findViewById(R.id.backButton)
        backText = view.findViewById(R.id.backText)

        // Initialize ViewModel with custom factory
        setupViewModel()

        // Setup listeners
        setupPasswordVisibilityToggle()
        btnContinue.setOnClickListener {
            Log.d("CreatePasswordFragment", "Continue button clicked")

            validateAndSubmitPassword()
        }

        observeViewModel()
        setupBackNavigation()

        return view
    }
    private fun setupBackNavigation() {
        backButton.setOnClickListener { findNavController().navigate(R.id.action_resetPasswordFragment_to_emailFragment) }
        backText.setOnClickListener { findNavController().navigate(R.id.action_resetPasswordFragment_to_emailFragment) }
    }

    private fun setupViewModel() {
        val apiService = RetrofitInstance.api
        val repository = ForgotPasswordRepository(apiService)
        val factory = ForgotPasswordViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]
    }

    private fun setupPasswordVisibilityToggle() {
        newPasswordToggle.setOnClickListener {
            isNewPasswordVisible = !isNewPasswordVisible
            togglePasswordVisibility(newPasswordInput, newPasswordToggle, isNewPasswordVisible)
        }

        confirmPasswordToggle.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(confirmPasswordInput, confirmPasswordToggle, isConfirmPasswordVisible)
        }
    }

    private fun togglePasswordVisibility(editText: EditText, toggleIcon: ImageView, isVisible: Boolean) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            toggleIcon.setImageResource(R.drawable.ic_visibility_on)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleIcon.setImageResource(R.drawable.ic_visibility_off)
        }
        editText.setSelection(editText.text.length) // Move cursor to end
    }

    private fun validateAndSubmitPassword() {
        val newPassword = newPasswordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()

        Log.d("CreatePasswordFragment", "Validating password")
        Log.d("CreatePasswordFragment", "New Password: $newPassword, Confirm Password: $confirmPassword")

        errorMessage.visibility = View.GONE

        when {
            newPassword.isBlank() || confirmPassword.isBlank() -> {
                errorMessage.text = "Both password fields must be filled."
                errorMessage.visibility = View.VISIBLE
            }
            newPassword != confirmPassword -> {
                errorMessage.text = "Passwords do not match. Please try again."
                errorMessage.visibility = View.VISIBLE
            }
            !isPasswordStrong(newPassword) -> {
                errorMessage.text = "Password must be at least 8 characters long and contain a mix of letters, numbers, and special characters."
                errorMessage.visibility = View.VISIBLE
            }
            else -> {
                val email = SharedPreferencesHelper.getEmail(requireContext()) ?: run {
                    Log.e("CreatePasswordFragment", "Email is null")
                    return
                }

                Log.d("CreatePasswordFragment", "Submitting password for email: $email")
                viewModel.setPassword(email, newPassword)
            }
        }
    }


    private fun isPasswordStrong(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$")
        return password.matches(passwordPattern)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    Log.d("CreatePasswordFragment", "UI State observed: $uiState")
                    when (uiState) {
                        is PasswordUiState.Loading -> {
                            Log.d("CreatePasswordFragment", "Loading state")
                            btnContinue.isEnabled = false
                        }
                        is PasswordUiState.Success -> {
                            Log.d("CreatePasswordFragment", "Password reset success")
                            btnContinue.isEnabled = true
                            showSuccessDialogAndNavigate()
                        }
                        is PasswordUiState.Error -> {
                            Log.e("CreatePasswordFragment", "Error state: ${uiState.message}")
                            btnContinue.isEnabled = true
                            showErrorDialog(uiState.message)
                        }
                        PasswordUiState.Idle -> {
                            Log.d("CreatePasswordFragment", "Idle state")
                        }
                    }
                }
            }
        }

    }


    private fun showSuccessDialogAndNavigate() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_password_success, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialog.show()

        // Automatically dismiss the dialog and navigate after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            navigateToLoginScreen()
        }, 5000) // 5-second delay
    }

    private fun navigateToLoginScreen() {
//        findNavController().navigate(R.id.action_createPasswordFragment_to_loginFragment)

    }

    private fun showErrorDialog(message: String) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_password_error, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialog.show()

        dialogView.findViewById<TextView>(R.id.text_error_message).text = message
        dialogView.findViewById<TextView>(R.id.text_try_again).setOnClickListener {
            dialog.dismiss()
        }
    }
}
