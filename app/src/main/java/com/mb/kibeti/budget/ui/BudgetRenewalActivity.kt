package com.mb.kibeti.budget.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mb.kibeti.budget.repository.CurrentBudgetRepo
import com.mb.kibeti.budget.repository.EditedBudgetRepo
import com.mb.kibeti.budget.viewmodel.CurrentBudgetViewModel
import com.mb.kibeti.budget.viewmodel.CurrentBudgetViewModelFactory
import com.mb.kibeti.budget.viewmodel.EditedBudgetViewModel
import com.mb.kibeti.budget.viewmodel.EditedBudgetViewModelFactory
import com.mb.kibeti.databinding.ActivityBudgetRenewalBinding
import java.util.Calendar
import java.util.Locale


class BudgetRenewalActivity : AppCompatActivity() {

    private lateinit var bind: ActivityBudgetRenewalBinding
    private lateinit var budgetData: Map<String, String> // Declare class-level budgetData


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityBudgetRenewalBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //disabling buttons to avoid null poiter errors(buttons should wait for data to be fetched
        bind.editBudgetButton.isEnabled = false
        bind.editBudgetButton.isEnabled = false


        // Get current month and year dynamically
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-based
        val currentYear = calendar.get(Calendar.YEAR)
        calendar.add(Calendar.MONTH, 1) // Move to the next month
        val nextMonth: String =
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                ?: "Unknown Month"


// dynamically setting the texts on the buttons
        bind.editBudgetButton.setText("Edit this budget and use it for $nextMonth")
        bind.sameBudgetForJanuary.setText("Keep this budget for $nextMonth")
        bind.createNewBudgetBtn.setText("Create new budget for $nextMonth")


        // Define action and user details

        val email = "sophiamatano10@gmail.com"


        // Initialize repository and ViewModel
        var action = "get_budget"
        val repo = CurrentBudgetRepo(action, email, currentMonth, currentYear)
        val viewModel by viewModels<CurrentBudgetViewModel> {
            CurrentBudgetViewModelFactory(repo)
        }

        // Handle edit button click


        // Observe  current budget data
        viewModel.currentBalanceResult.observe(this, Observer { result ->
            if (result.isSuccessful && result.body() != null) {
                val body = result.body()!!
                val totalInflow = body.total_inflow
                val inflowSuccess = body.inflow_success
                val outflow = body.outflow

                if (outflow != null && outflow.isNotEmpty()) {
                    val firstOutFlow = outflow[0]

                    // Set text views with data
                    bind.tvRecAmount1.text = firstOutFlow.rec_amt_1.toString()
                    bind.tvAmount1.text = firstOutFlow.amount_1
                    if (firstOutFlow.amount_1 > firstOutFlow.rec_amt_1) {
                        bind.tvAmount1.setTextColor(Color.RED)
                    }

                    bind.tvAmount2.text = firstOutFlow.amount_2
                    bind.tvRecAmount2.text = firstOutFlow.rec_amt_2.toString()

                    // check if spent amount is greater than recoded amount and set spent amount text to red
                    if (firstOutFlow.amount_2 > firstOutFlow.rec_amt_2) {
                        bind.tvAmount2.setTextColor(Color.RED)
                    }

                    bind.tvAmount3.text = firstOutFlow.amount_3
                    bind.tvRecAmount3.text = firstOutFlow.rec_amt_3.toString()
                    if (firstOutFlow.amount_3 > firstOutFlow.rec_amt_3) {
                        bind.tvAmount3.setTextColor(Color.RED)
                    }


                    bind.tvAmount4.text = firstOutFlow.amount_4
                    bind.tvRecAmount4.text = firstOutFlow.rec_amt_4.toString()
                    if (firstOutFlow.amount_4 > firstOutFlow.rec_amt_4) {
                        bind.tvAmount4.setTextColor(Color.RED)
                    }


                    bind.tvAmount5.text = firstOutFlow.amount_5
                    bind.tvRecAmount5.text = firstOutFlow.rec_amt_5.toString()
                    if (firstOutFlow.amount_5 > firstOutFlow.rec_amt_5) {
                        bind.tvAmount5.setTextColor(Color.RED)
                    }



                    bind.tvAmount6.text = firstOutFlow.amount_6
                    bind.tvRecAmount6.text = firstOutFlow.rec_amt_6.toString()
                    if (firstOutFlow.amount_6 > firstOutFlow.rec_amt_6) {
                        bind.tvAmount6.setTextColor(Color.RED)
                    }



                    bind.tvAmount7.text = firstOutFlow.amount_7
                    bind.tvRecAmount7.text = firstOutFlow.rec_amt_7.toString()
                    if (firstOutFlow.amount_7 > firstOutFlow.rec_amt_7) {
                        bind.tvAmount7.setTextColor(Color.RED)
                    }



                    bind.tvAmount8.text = firstOutFlow.amount_8
                    bind.tvRecAmount8.text = firstOutFlow.rec_amt_8.toString()
                    if (firstOutFlow.amount_8 > firstOutFlow.rec_amt_8) {
                        bind.tvAmount8.setTextColor(Color.RED)
                    }


                    bind.tvAmount9.text = firstOutFlow.amount_9
                    bind.tvRecAmount9.text = firstOutFlow.rec_amt_9.toString()
                    if (firstOutFlow.amount_9 > firstOutFlow.rec_amt_9) {
                        bind.tvAmount9.setTextColor(Color.RED)
                    }


                    bind.tvAmount10.text = firstOutFlow.amount_10
                    bind.tvRecAmount10.text = firstOutFlow.rec_amt_10.toString()
                    if (firstOutFlow.amount_10 > firstOutFlow.rec_amt_10) {
                        bind.tvAmount10.setTextColor(Color.RED)
                    }


                    // Prepare the budget data map for use in the dialog
                    budgetData = mapOf(
                        "Savings" to (firstOutFlow.rec_amt_1 ?: "0.0"),
                        "Investments" to (firstOutFlow.rec_amt_2 ?: "0.0"),
                        "Day to day" to (firstOutFlow.rec_amt_3 ?: "0.0"),
                        "Mobility" to (firstOutFlow.rec_amt_4 ?: "0.0"),
                        "Faith and giving" to (firstOutFlow.rec_amt_5 ?: "0.0"),
                        "Self" to (firstOutFlow.rec_amt_6 ?: "0.0"),
                        "Loan Servicing" to (firstOutFlow.rec_amt_7 ?: "0.0"),
                        "Protection" to (firstOutFlow.rec_amt_8 ?: "0.0"),
                        "Dependents" to (firstOutFlow.rec_amt_9 ?: "0.0"),
                        "Other Outflows" to (firstOutFlow.rec_amt_10 ?: "0.0")
                    )

                    //enabling buttons that were disabled(enabled when the map is set successfully to avoid null pointer)
                    bind.editBudgetButton.isEnabled = true
                    bind.editBudgetButton.isEnabled = true


                    //setting data needed to hit endpoint for coping budget to next month
                    action = "post_budget"
                    val (nextMonthNumeric, nextYear) = getNextMonthAndYear()
                    val month = nextMonthNumeric
                    val year = nextYear
                    val day = 0
                    val dependents = budgetData["Dependents"]?.toIntOrNull()
                        ?: 0
                    val savings = budgetData["Savings"]?.toIntOrNull()
                        ?: 0
                    val investments = budgetData["Investments"]?.toIntOrNull()
                        ?: 0
                    val mobility = budgetData["Mobility"]?.toIntOrNull()
                        ?: 0
                    val giving = budgetData["Faith and giving"]?.toIntOrNull()
                        ?: 0
                    val self = budgetData["Self"]?.toIntOrNull()
                        ?: 0
                    val loan = budgetData["Loan Servicing"]?.toIntOrNull()
                        ?: 0
                    val protection = budgetData["Protection"]?.toIntOrNull()
                        ?: 0
                    val others = budgetData["Other Outflows"]?.toIntOrNull() ?: 0  //


//initializing repository and viewmodel for hitting endpoint for saving copied budget
                    val editBudgetRepo = EditedBudgetRepo(
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
                    val editBudgetViewModel by viewModels<EditedBudgetViewModel> {
                        EditedBudgetViewModelFactory(editBudgetRepo)

                    }
                    bind.editBudgetButton.setOnClickListener {
                        openEditBudgetDialog()
                    }

                    bind.sameBudgetForJanuary.setOnClickListener {
                        editBudgetViewModel.saveEditedBudget()
                    }
//observe results from hitting the save budget endpoint
                    editBudgetViewModel.editedBudegetResult.observe(this, Observer { result ->
                        if (result.isSuccessful && result.body() != null) {


                            Toast.makeText(this, "${result.body()!!.message}", Toast.LENGTH_SHORT)
                                .show()
                        }else{
                            Toast.makeText(this, "${result.errorBody()}", Toast.LENGTH_SHORT).show()

                        }

                    })


                } else {
                    Toast.makeText(this, "Outflow data is null or empty", Toast.LENGTH_SHORT).show()
                }

                // Display a toast with the inflow success message
                Toast.makeText(this, "Inflow success: $inflowSuccess", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(
                    this,
                    "No result or error in fetching budget data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
//preparing properties needed for hitting endpoint to save current budget for next month


    }

    fun getNextMonthAndYear(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1) // Move to the next month
        val nextMonthNumeric = calendar.get(Calendar.MONTH) + 1 // 1-based month
        val nextYear = calendar.get(Calendar.YEAR)
        return Pair(nextMonthNumeric, nextYear)
    }


    // Function to open the Edit Budget dialog
    private fun openEditBudgetDialog() {
        val dialog = EditCurrentBudgetFragment(budgetData)
        dialog.show(supportFragmentManager, "EditBudgetBottomSheet")


    }
}
