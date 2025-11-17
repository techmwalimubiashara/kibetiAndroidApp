package com.mb.kibeti.budget.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mb.kibeti.budget.repository.CurrentBudgetRepo
import com.mb.kibeti.budget.repository.EditedBudgetRepo
import com.mb.kibeti.budget.viewmodel.CurrentBudgetViewModel
import com.mb.kibeti.budget.viewmodel.CurrentBudgetViewModelFactory
import com.mb.kibeti.budget.viewmodel.EditedBudgetViewModel
import com.mb.kibeti.budget.viewmodel.EditedBudgetViewModelFactory
import com.mb.kibeti.R
import com.mb.kibeti.databinding.FragmentEditCurrentBudgetBinding
//import com.mb.kibeti.landingPageTask.utils.PREFERENCES
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Locale


fun EditText.bindCurrencyFormatter() {
    this.addTextChangedListener(object : TextWatcher {
        private var current = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != current) {
                this@bindCurrencyFormatter.removeTextChangedListener(this)

                val cleanString = s.toString().replace(",", "")
                if (cleanString.isNotEmpty()) {
                    val parsed = cleanString.toDouble()
                    val formatted = DecimalFormat("#,###").format(parsed)

                    current = formatted
                    this@bindCurrencyFormatter.setText(formatted)
                    this@bindCurrencyFormatter.setSelection(formatted.length)
                }

                this@bindCurrencyFormatter.addTextChangedListener(this)
            }
        }
    })
}


class EditCurrentBudgetFragment(private val budgetData: Map<String, String>) :
    BottomSheetDialogFragment() {
    private lateinit var bind: FragmentEditCurrentBudgetBinding


    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentEditCurrentBudgetBinding.inflate(inflater, container, false)

        //get calendar instance
        val calendar = Calendar.getInstance()

         // Increment the month
        calendar.add(Calendar.MONTH, 1)

        val nextMonthName: String =
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                ?: "Unknown Month"
        bind.idBtnSave.setText("Save Changes For $nextMonthName")

// set the values from the map to be displayed on the edi texts
        bind.edItem1.setText(budgetData["Savings"])
        bind.edItem2.setText(budgetData["Investments"])
        bind.edItem3.setText(budgetData["Day to day"])
        bind.edItem4.setText(budgetData["Mobility"])
        bind.edItem5.setText(budgetData["Faith and giving"])
        bind.edItem6.setText(budgetData["Self"])
        bind.edItem7.setText(budgetData["Loan Servicing"])
        bind.edItem8.setText(budgetData["Protection"])
        bind.edItem9.setText(budgetData["Dependents"])
        bind.edItem10.setText(budgetData["Other Outflows"])

        bind.edItem1.bindCurrencyFormatter()
        bind.edItem2.bindCurrencyFormatter()
        bind.edItem3.bindCurrencyFormatter()
        bind.edItem4.bindCurrencyFormatter()
        bind.edItem5.bindCurrencyFormatter()
        bind.edItem6.bindCurrencyFormatter()
        bind.edItem7.bindCurrencyFormatter()
        bind.edItem8.bindCurrencyFormatter()
        bind.edItem9.bindCurrencyFormatter()
        bind.edItem10.bindCurrencyFormatter()

        val (nextMonth, nextYear) = getNextMonthAndYear()
        // creating values needed in the request body
        val email = "sophiamatano10@gmail.com"
        val action = "post_budget"
        val month = nextMonth
        val year = nextYear
        val day = 0
        val dependents = bind.edItem9.text.toString().replace(",", "").toIntOrNull() ?: 0
        val savings = bind.edItem1.text.toString().replace(",", "").toIntOrNull() ?: 0
        val investments = bind.edItem2.text.toString().replace(",", "").toIntOrNull() ?: 0
        val mobility = bind.edItem4.text.toString().replace(",", "").toIntOrNull() ?: 0
        val giving = bind.edItem5.text.toString().replace(",", "").toIntOrNull() ?: 0
        val self = bind.edItem6.text.toString().replace(",", "").toIntOrNull() ?: 0
        val loan = bind.edItem7.text.toString().replace(",", "").toIntOrNull() ?: 0
        val protection = bind.edItem8.text.toString().replace(",", "").toIntOrNull() ?: 0
        val others = bind.edItem10.text.toString().replace(",", "").toIntOrNull() ?: 0


        // initializing repository and view modrl to be used to hit endpoint
        val repo = EditedBudgetRepo(
            email,
            action,
            month,
            year,
            day,
            dependents,
            savings,
            investments,
            mobility,
            giving,
            self,
            loan,
            protection,
            others
        )
        val viewModel by viewModels<EditedBudgetViewModel> {
            EditedBudgetViewModelFactory(repo)

        }

        //clicking button to triger function to hit endpoint
        bind.idBtnSave.setOnClickListener {
            viewModel.saveEditedBudget()
        }

        //observing response
        viewModel.editedBudegetResult.observe(viewLifecycleOwner, Observer { result ->
            if (result.isSuccessful) {
                Toast.makeText(requireContext(), " ${result.body()!!.message}", Toast.LENGTH_SHORT)
                    .show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Error posting ", Toast.LENGTH_SHORT).show()

            }

        })

        return bind.root
    }

    fun getNextMonthAndYear(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1) // Move to the next month
        val nextMonth = calendar.get(Calendar.MONTH) + 1 // 1-based month
        val nextYear = calendar.get(Calendar.YEAR)
        return Pair(nextMonth, nextYear)
    }


}