package com.mb.kibeti.invest_guide

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mb.kibeti.R

class EmergencyFundFragment : Fragment() {

    private lateinit var tvInvestableAmount: TextView
    private lateinit var tvMonthlyExpenses: TextView
    private lateinit var tvTotal: TextView
    private lateinit var etMonths: EditText
    private lateinit var investBtn: Button
    private lateinit var tvError: TextView
    private var availableFunds: Float = 0f
    private var monthlyExpenses: Float = 0f
    private var investableAmount: Float = 0f
//    private lateinit var sharedPrefs: SharedPreferences //= requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
//    private

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_emergency_fund, container, false)

        tvInvestableAmount = view.findViewById(R.id.tvInvestableAmount)
        tvTotal = view.findViewById(R.id.tvTotalAmount)
        tvMonthlyExpenses = view.findViewById(R.id.tvMonthlyExpenses)
        etMonths = view.findViewById(R.id.etMonths)
        investBtn = view.findViewById(R.id.investBtn)
        tvError = view.findViewById(R.id.tvError)
        var sharedPrefs: SharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        availableFunds = sharedPrefs.getFloat("availableFunds", 0f)
        monthlyExpenses = sharedPrefs.getFloat("monthlyExpenses", 0f)

        updateInvestableAmount(0)

        investBtn.setOnClickListener {
            if (investableAmount > 0) {
                sharedPrefs.edit().putFloat("investableAmount", investableAmount).apply()
                val intent = Intent(requireContext(), InvestmentModelActivity::class.java)
                startActivity(intent)
            }else{
                showAlertDialog()
            }
            }

        etMonths.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val months = s.toString().toIntOrNull() ?: 0
                updateInvestableAmount(months)
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        return view
    }


    private fun updateInvestableAmount(months: Int) {
        investableAmount = availableFunds - (monthlyExpenses * months)
        tvTotal.text = "The lumpsum amount I have = KES ${String.format("%,.0f", availableFunds)}"
        tvMonthlyExpenses.text = "My ${months} months expenses = KES ${String.format("%,.0f", (monthlyExpenses * months))}"
        tvInvestableAmount.text = "The amount I can invest = KES ${String.format("%,.0f", investableAmount)}"

        if (investableAmount<0){
            tvInvestableAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }else{
            tvInvestableAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
        }

        // Disable button and show error if investable amount is negative
        if (investableAmount <= 0) {
            investBtn.isEnabled = true
            tvError.visibility = View.VISIBLE
            tvError.text = "You have no funds to invest. Please adjust your expenses."

//            investBtn.setOnClickListener {
//                showAlertDialog()
//            }

        } else {
            investBtn.isEnabled = true
            tvError.visibility = View.GONE
//            investBtn.setOnClickListener {
//                checkInvestments()
//            }
        }
    }
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("No fund to invest")
        builder.setMessage("You have no funds to invest. Please adjust your expenses.")


        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}
