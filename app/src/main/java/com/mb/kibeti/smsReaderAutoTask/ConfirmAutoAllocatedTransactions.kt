package com.mb.kibeti.smsReaderAutoTask

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mb.kibeti.R
import com.mb.kibeti.databinding.ActivityConfirmAutoAllocatedTransactionsBinding
import com.mb.kibeti.smsReaderAutoTask.repositories.ConfirmAllocatedTransactionsRepo
import com.mb.kibeti.smsReaderAutoTask.repositories.GetAutoAllocatedTransactionsRepo
import com.mb.kibeti.smsReaderAutoTask.viewmodels.ConfirmAllocatedTransactionsViewModel
import com.mb.kibeti.smsReaderAutoTask.viewmodels.ConfirmAllocatedTransactionsViewModelFactory
import com.mb.kibeti.smsReaderAutoTask.viewmodels.GetAutoAllocatedTransactionsViewModel
import com.mb.kibeti.smsReaderAutoTask.viewmodels.GetAutoAllocatedTransactionsViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConfirmAutoAllocatedTransactions : AppCompatActivity() {
    private lateinit var bind: ActivityConfirmAutoAllocatedTransactionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityConfirmAutoAllocatedTransactionsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        val email = "admin@kibeti.com"
        val date = "2025-02-21"
        val action = "confirm"
        Log.d("currentDateToday", date)
        val repo = GetAutoAllocatedTransactionsRepo(action, date, email)
        val viewModel by viewModels<GetAutoAllocatedTransactionsViewModel> {
            GetAutoAllocatedTransactionsViewModelFactory(repo)
        }

        viewModel.autoAllocateTransactionsResults.observe(this, Observer { result ->
            if (result.isSuccessful && result.body() != null) {
                Toast.makeText(this, "${result.body()!!.response} ", Toast.LENGTH_SHORT).show()
                val transactionsAdapter = ConfirmationTransactionListAdapter(result.body()!!.data)
                bind.recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@ConfirmAutoAllocatedTransactions)
                    adapter = transactionsAdapter
                    setHasFixedSize(true)

                }
            }
        })

        val action1 = "confirm_all"
        val confirmRepo = ConfirmAllocatedTransactionsRepo(action1, date, email)
        val confirmViewModel by viewModels<ConfirmAllocatedTransactionsViewModel> {
            ConfirmAllocatedTransactionsViewModelFactory(confirmRepo)

        }


        bind.btnConfirmAll.setOnClickListener {
            confirmViewModel.confirmAllocatedTransactions()
        }
        confirmViewModel.confirmAutoAllocatedTransactionsResult.observe(this, Observer { result ->
            if (result.isSuccessful && result.body() != null) {
                Toast.makeText(this, "${result.body()!!.message} ", Toast.LENGTH_SHORT).show()

                refreshData()
            }

        })


    }

    fun refreshData() {
        // After confirmation, call the backend again to reload the (updated) data from the server
        val email = "admin@kibeti.com"
        val date = "2025-02-21"
        val action = "confirm"

        // Assuming you are using the same repo to fetch the data
        val repo = GetAutoAllocatedTransactionsRepo(action, date, email)
        val viewModel by viewModels<GetAutoAllocatedTransactionsViewModel> {
            GetAutoAllocatedTransactionsViewModelFactory(repo)
        }

        viewModel.autoAllocateTransactionsResults.observe(this, Observer { result ->
            if (result.isSuccessful && result.body() != null) {
                // Here, you will receive the updated response after the backend clears the data
                if (result.body()!!.data.isEmpty()) {
                    // If the data is empty (transactions are cleared), set an empty adapter
                    val transactionsAdapter = ConfirmationTransactionListAdapter(emptyList())
                    bind.recyclerView.adapter = transactionsAdapter
                } else {
                    // If there are still transactions, update the adapter with the new data
                    val transactionsAdapter = ConfirmationTransactionListAdapter(result.body()!!.data)
                    bind.recyclerView.adapter = transactionsAdapter
                }
            }
        })
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

}