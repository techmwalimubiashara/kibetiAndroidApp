package com.mb.kibeti.sms_filter.whatsapp_number

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.LoginActivity.EMAIL
import com.mb.kibeti.LoginActivity.MY_PREFERENCES
import com.mb.kibeti.databinding.ActivityTipsBinding
import com.mb.kibeti.sms_filter.JoinWhatsappActivity


class TipsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipsBinding
    private val viewModel: TipsViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private  var email: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityTipsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE)
        email = sharedPreferences.getString(EMAIL, "")

        setupObservers()
        setupClickListeners()
        setupBackNavigation()
    }

    private fun setupObservers() {
        viewModel.apiResponse.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
//                    when {
//                        response.data?.isSuccessfulOperation() == true -> {
////                            showSuccess("Registration successful!")
//                            navigateToNextScreen()
//                        }
//                        response.data?.isAlreadyRegistered() == true -> {
//                            // Silent navigation for already registered numbers
//                            navigateToNextScreen()
//                        }
//                        else -> {
//                            navigateToNextScreen()
//                        }
//                    }

                    navigateToNextScreen()
                }
                is Resource.Error -> {
                    hideLoading()
                    showError(response.message ?: "An error occurred")
                }
                is Resource.Loading -> showLoading()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnNextTransactions.setOnClickListener {
            val number = binding.editWhatsAppNumber.text.toString().trim()

            if (!isValidPhoneNumber(number)) {
                binding.editWhatsAppNumber.error = "Please enter a valid phone number\n" +
                        "Formats: 0722123456 or 254722123456 or +254722123456"
                return@setOnClickListener
            }

            val internationalNumber = convertToInternationalFormat(number)
            viewModel.joinCommunity(email,internationalNumber)
            navigateToNextScreen()
        }
    }

    private fun isValidPhoneNumber(number: String): Boolean {
        return number.matches(Regex("^(?:0|\\+?254)\\d{9}$"))
    }

    private fun convertToInternationalFormat(phoneNumber: String): String {
        return when {
            phoneNumber.startsWith("0") -> "+254${phoneNumber.substring(1)}"
            phoneNumber.startsWith("254") -> "+$phoneNumber"
            phoneNumber.startsWith("+254") -> phoneNumber
            else -> phoneNumber // fallback, though validation should prevent this
        }
    }

    private fun showLoading() {
        binding.loadingOverlay.root.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideLoading() {
        binding.loadingOverlay.root.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToNextScreen() {
//        startActivity(Intent(this, JoinWhatsappActivity::class.java))

        val intent = Intent(this, JoinWhatsappActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupBackNavigation() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
}