package com.mb.kibeti.invest_guide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.ChartActivity
import com.mb.kibeti.R

class InvestmentModelActivity : AppCompatActivity() {

    private val investmentViewModel: InvestmentViewModel by viewModels()
    private lateinit var investments: List<Investment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_investment_model)
        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val amountAvailable = sharedPreferences.getFloat("investableAmount", 0.0f)

        investmentViewModel.setAvailableAmount(amountAvailable)

        val tvAvailableAmount: TextView = findViewById(R.id.tvAvailableAmount)
        val calculateButton: Button = findViewById(R.id.calculateButton)
        val ivClose: ImageView = findViewById(R.id.ivClose)

        // Set the OnClickListener for the back icon to navigate to the previous activity
        ivClose.setOnClickListener {
            // You can use finish() to go back to the previous activity
            finish()
        }

        // Observe available amount and update UI dynamically
        investmentViewModel.availableAmount.observe(this) { available ->
            tvAvailableAmount.text = "Available Balance: KES ${String.format("%,.0f", available)}"

            // Enable or disable the Calculate button
            calculateButton.isEnabled = available >= 0
            calculateButton.alpha = if (available >= 0) 1.0f else 0.5f

            // Show validation error if the available balance is negative
            if (available < 0) {
                showValidationError(amountAvailable)
            }
        }

        val investments = listOf(
            Investment("Bank Deposits", "Saving your money in a commercial bank"),
            Investment("Stock Market", "Placing your money in a pool that is managed by a professional fund manager"),
            Investment("Treasury Bills", "Lending your money to the government for a period of less than 1 year"),
            Investment("Infrastructure Bonds", "Lending your money to the government for a long time so you get payments every 6 months."),
            Investment("Commercial Properties", "Buying a plot or building houses for rental"),
            Investment("REIT", "Placing your money in a pool that is managed by a professional fund manager to develop properties"),
            Investment("Shares", "Buying listed shares e.g. Safaricom, KCB, Equity"),
            Investment("Small Business", "Start a business or partner with someone already running a business"),
            Investment("Forex Trading", "Buying and selling currencies"),
            Investment("SACCO", "Placing your money in a SACCO")
        )

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = InvestmentAdapter(
            investments,
            onInfoClicked = { info -> showInvestmentInfo(info) },
            onAmountChanged = { position, newAmount ->
                investmentViewModel.updateAmount(position, newAmount)
            }
        )

        recyclerView.adapter = adapter

        // Set an action for the Calculate button
        calculateButton.setOnClickListener {
            val selectedInvestments = investments
                .filter { it.amount != null && it.amount!! > 0 }
                .map { InvestmentData(it.name, it.amount!!, 0) } // Default frequency to 0

            Log.d("InvestmentModelActivity", "Selected Investments: $selectedInvestments")

            if (selectedInvestments.isNotEmpty()) {
                //Replace the inputscreenactivity to the intended activity
                val intent = Intent(this, ChartActivity::class.java).apply {
                    putExtra("selectedInvestments", ArrayList(selectedInvestments))
                }
                startActivity(intent)
            } else {
                showValidationError(0f)
            }
        }
    }

    private fun showValidationError(amountAvailable: Float) {
        AlertDialog.Builder(this)
            .setTitle("Validation Error")
            .setMessage("The total investments cannot exceed KES ${String.format("%,.0f", amountAvailable)}.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showInvestmentInfo(info: String) {
        AlertDialog.Builder(this)
            .setTitle("Investment Information")
            .setMessage(info)
            .setPositiveButton("OK", null)
            .show()
    }
}
