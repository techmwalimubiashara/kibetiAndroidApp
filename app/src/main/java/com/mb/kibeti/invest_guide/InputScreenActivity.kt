package com.mb.kibeti.invest_guide

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.R
import java.util.*

class InputScreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_screen)
        supportActionBar?.hide()

//        sharedPreferences = getSharedPreferences("PlannerAppPrefs", Context.MODE_PRIVATE)
//
//        val etCapitalOutlay = findViewById<EditText>(R.id.etCapitalOutlay)
//        val etMonthlyOutflow = findViewById<EditText>(R.id.etMonthlyOutflow)
//        val etMonths = findViewById<EditText>(R.id.etMonths)
//        val tvAmountAvailable = findViewById<TextView>(R.id.tvAmountAvailable)
//        val tvError = findViewById<TextView>(R.id.tvError) // Error message TextView
//        val btnNext = findViewById<Button>(R.id.btnNext)
//
//        // Initially disable the Next button
//        btnNext.isEnabled = false
//
//        // Add number formatting
//        addNumberFormatting(etCapitalOutlay)
//        addNumberFormatting(etMonthlyOutflow)
//
//        val textWatcher = object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                validateAndCalculateAmount(
//                    etCapitalOutlay.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0,
//                    etMonthlyOutflow.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0,
//                    etMonths.text.toString().toIntOrNull() ?: 0,
//                    tvAmountAvailable,
//                    tvError,
//                    btnNext
//                )
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        }
//
//        etCapitalOutlay.addTextChangedListener(textWatcher)
//        etMonthlyOutflow.addTextChangedListener(textWatcher)
//        etMonths.addTextChangedListener(textWatcher)
//
//        btnNext.setOnClickListener {
//            if (tvError.text.isEmpty()) {
//                val intent = Intent(this, InvestmentModelActivity::class.java)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Please correct the errors before proceeding", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun validateAndCalculateAmount(
//        capitalOutlay: Double,
//        monthlyOutflow: Double,
//        months: Int,
//        tvAmountAvailable: TextView,
//        tvError: TextView,
//        btnNext: Button
//    ) {
//        if (capitalOutlay < 0 || monthlyOutflow < 0 || months < 0) {
//            tvError.text = "Input values cannot be negative."
//            tvAmountAvailable.text = "Investable Amount: --"
//            btnNext.isEnabled = false
//            return
//        }
//
//        val amountAvailable = capitalOutlay - (monthlyOutflow * months)
//
//        if (amountAvailable < 0) {
//            tvError.text = "Your expenses exceed your availsble funds. Adjust your budget to find the right balance and achieve your goals!"
//            tvAmountAvailable.text = "Investable Amount: --"
//            btnNext.isEnabled = false
//        } else {
//            tvError.text = ""
//            val formattedAmount = formatNumber(amountAvailable)
//            tvAmountAvailable.text = "Investable Amount: $formattedAmount"
//            btnNext.isEnabled = true
//
//            sharedPreferences.edit().putFloat("amountAvailable", amountAvailable.toFloat()).apply()
//            Log.d("Saved Investable Amount", amountAvailable.toString())
//        }
//    }
//
//    private fun addNumberFormatting(editText: EditText) {
//        editText.addTextChangedListener(object : TextWatcher {
//            private var currentText = ""
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val input = s.toString()
//
//                if (input == currentText) return
//
//                editText.removeTextChangedListener(this)
//
//                // Remove commas for clean parsing
//                val cleanInput = input.replace(",", "")
//
//                try {
//                    // Format with commas
//                    val formatted = cleanInput.toLongOrNull()?.let {
//                        formatNumber(it.toDouble())
//                    } ?: cleanInput
//
//                    currentText = formatted
//                    editText.setText(formatted)
//                    editText.setSelection(formatted.length) // Move cursor to the end
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//                editText.addTextChangedListener(this)
//            }
//        })
//    }
//
//    private fun formatNumber(value: Double): String {
//        return NumberFormat.getNumberInstance(Locale.getDefault()).format(value)
    }
}
