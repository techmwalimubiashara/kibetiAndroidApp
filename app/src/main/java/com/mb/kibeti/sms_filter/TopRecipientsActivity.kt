package com.mb.kibeti.sms_filter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mb.kibeti.R
import com.mb.kibeti.databinding.ActivityTopRecipientsBinding
import com.mb.kibeti.sms_filter.TransactionViewModel
import java.text.NumberFormat
import java.util.*

class TopRecipientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopRecipientsBinding
    private lateinit var viewModel: TransactionViewModel

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            viewModel.loadInitialData(this)
        } else {
            Toast.makeText(
                this,
                "SMS permissions required for analysis",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityTopRecipientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        binding.recyclerTopRecipients.layoutManager = LinearLayoutManager(this)

        // Observe total spending
        viewModel.totalSpending.observe(this) { total ->
            binding.textTotalSpending.text =
                "Check out the top places you spent money in the last 6 months"
//                "Did you know you spent ${CurrencyUtils.formatAmount(this, total ?: 0.0)} on shopping in the last 6 months?"
        }

        // Observe top recipients
        viewModel.topRecipients.observe(this) { recipients ->
            binding.recyclerTopRecipients.adapter = TopRecipientAdapter(recipients, this)
        }

        // Handle Next Tip button click
        binding.btnNextTip.setOnClickListener {
            startActivity(Intent(this, SupermarketRecipientsActivity::class.java))
        }

        setupBackNavigation()

        checkSmsPermissions()
    }

    private fun setupBackNavigation() {
        // For the back button
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // For system back gesture/button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Add any custom back handling if needed
                finish()
            }
        })
    }

    private fun checkSmsPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )

        if (requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            viewModel.loadInitialData(this)
        } else {
            permissionLauncher.launch(requiredPermissions)
        }
    }
}