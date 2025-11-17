package com.mb.kibeti.sms_filter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.MainActivity
import com.mb.kibeti.PipeLines
import com.mb.kibeti.databinding.ActivityJoinWhatsappBinding
import com.mb.kibeti.goal_tracker.GoalTrackerActivity
import com.mb.kibeti.Constants
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
class JoinWhatsappActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinWhatsappBinding

    private var userTriedToJoin = false // Track if user clicked the Join button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityJoinWhatsappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnJoinWhatsapp.setOnClickListener {
            userTriedToJoin = true // Mark that user tried joining
            openWhatsappGroup()
        }

        binding.btnSkip.setOnClickListener {
            navigateToDashboard()
        }
    }

    private fun openWhatsappGroup() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.whatsappGroupLink)
            startActivity(intent)
        } catch (e: Exception) {
            // WhatsApp not installed, open in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://web.whatsapp.com"))
            startActivity(browserIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        // If user clicked the join button and returned to this screen, navigate to dashboard
        if (userTriedToJoin) {
            markAsJoined()
            navigateToDashboard()
        }
    }

    private fun markAsJoined() {
        val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("hasJoinedWhatsapp", true)
            apply()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}
