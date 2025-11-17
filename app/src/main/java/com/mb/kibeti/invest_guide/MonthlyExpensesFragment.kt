package com.mb.kibeti.invest_guide

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mb.kibeti.R
import java.text.DecimalFormat

class MonthlyExpensesFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monthly_expenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        val tvAvailableAmount = view.findViewById<TextView>(R.id.tvAvailableAmount)
        val etAmount = view.findViewById<EditText>(R.id.etAmount)
        val nextBtn = view.findViewById<Button>(R.id.nextBtn)
        val errorTextView = view.findViewById<TextView>(R.id.errorTextView)

        // Retrieve the available funds as a Float
        val availableFunds = sharedPreferences.getFloat("availableFunds", 0f)
        tvAvailableAmount.text = "Available Amount KES ${formatAmount(availableFunds)}"

        etAmount.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    etAmount.removeTextChangedListener(this)

                    // Remove commas for clean processing
                    val cleanString = s.toString().replace(",", "")

                    // Format the text only if it is valid
                    val formatted = if (cleanString.isNotEmpty()) {
                        cleanString.toLongOrNull()?.let {
                            DecimalFormat("#,###").format(it)
                        } ?: cleanString
                    } else ""

                    current = formatted
                    etAmount.setText(formatted)

                    // Restore cursor position
                    etAmount.setSelection(formatted.length)

                    etAmount.addTextChangedListener(this)

                    // Validation: Check if the entered amount exceeds available funds
                    val enteredAmount = cleanString.toFloatOrNull() ?: 0f
                    if (enteredAmount > availableFunds) {
                        nextBtn.isEnabled = false
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.text = "Amount cannot exceed available funds"
                    } else {
                        nextBtn.isEnabled = true
                        errorTextView.visibility = View.GONE
                    }
                }
            }
        })

        nextBtn.setOnClickListener {
            val monthlyExpenses = etAmount.text.toString().replace(",", "").toFloatOrNull()
            if (monthlyExpenses != null && monthlyExpenses <= availableFunds) {
                sharedPreferences.edit().putFloat("monthlyExpenses", monthlyExpenses).apply()
                findNavController().navigate(R.id.action_monthlyExpensesFragment_to_emergencyFundFragment)
            } else {
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = "Please enter a valid amount that does not exceed available funds"
            }
        }
    }

    private fun formatAmount(amount: Float): String {
        return DecimalFormat("#,###").format(amount)
    }
}
