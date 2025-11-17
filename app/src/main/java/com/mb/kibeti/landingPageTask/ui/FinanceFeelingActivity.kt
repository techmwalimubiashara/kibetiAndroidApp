package com.mb.kibeti.landingPageTask.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.R

class FinanceFeelingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finance_feeling)
        supportActionBar?.hide()

        // Crisis Button
        findViewById<TextView>(R.id.btn_crisis).setOnClickListener {
            val intent = Intent(this, FinanceCrisisResponseActivity::class.java)
            startActivity(intent)
        }

        // Stressed Button
        findViewById<TextView>(R.id.btn_stressed).setOnClickListener {
            val intent = Intent(this, FinanceStressedResponseActivity::class.java)
            startActivity(intent)        }

        // Unsure Button
        findViewById<TextView>(R.id.btn_unsure).setOnClickListener {
            val intent = Intent(this, FinanceUnsureResponseActivity::class.java)
            startActivity(intent)        }

        // Stable Button
        findViewById<TextView>(R.id.btn_stable).setOnClickListener {
            val intent = Intent(this, FinanceStableResponseActivity::class.java)
            startActivity(intent)        }

        // Thriving Button
        findViewById<TextView>(R.id.btn_thriving).setOnClickListener {
            val intent = Intent(this, FinanceThrivingResponseActivity::class.java)
            startActivity(intent)        }
    }

    // Open Finance Response Activity
    private fun openResponseScreen(heading: String, body: String) {
        val intent = Intent(this, FinanceCrisisResponseActivity::class.java)
        intent.putExtra("heading", heading)
        intent.putExtra("body", body)
        startActivity(intent)
    }
}