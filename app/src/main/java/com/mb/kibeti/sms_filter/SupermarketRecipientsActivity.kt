package com.mb.kibeti.sms_filter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mb.kibeti.databinding.ActivitySupermarketRecipientsBinding
import com.mb.kibeti.sms_filter.whatsapp_number.TipsActivity
import java.util.Calendar

class SupermarketRecipientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySupermarketRecipientsBinding
    private lateinit var viewModel: TransactionViewModel
//    private val supermarketNames = listOf(
//        "Nakumatt", "Naivas", "Carrefour", "Chandarana", "Quickmart", "Cleanshelf",
//        "Magunas", "Uchumi", "Tuskys", "Eastmatt", "Shoprite", "Game", "Tumaini",
//        "Choppies", "Zuch", "Mulleys", "Khetias", "Eastmatt", "Galitos", "Foodplus",
//        "PURPLEMART", "PURPLE MART", "KWA MUKHWANA", "KWAMUKHWANA", "KWA HARRIET",
//        "KWAHARRIET", "KWA MUMO", "KWAMUMO", "KWA NJERI", "KWA NJOKI", "KWA WANJIRU"
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySupermarketRecipientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        setupUI()
        setupObservers()
        loadData()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnAction.setOnClickListener {
            startActivity(Intent(this, TipsActivity::class.java))
        }

        binding.recyclerSupermarkets.layoutManager = LinearLayoutManager(this)
        binding.recyclerSupermarkets.adapter = TopRecipientAdapter(emptyList(), this)
    }

    private fun setupObservers() {
        viewModel.supermarketTransactions.observe(this) { transactions ->
            if (transactions.isNotEmpty()) {
                val total = transactions.sumOf { it.totalAmount }
                binding.textTotalSpending.text = "Did you know you spent approximately ${formatCurrency(total)} on shopping in the last 6 months?"

                // Filter and sort the transactions
                val filteredTransactions = transactions
                    .filter { it.totalAmount > 0 }
                    .sortedByDescending { it.totalAmount }

                val adapter = binding.recyclerSupermarkets.adapter as TopRecipientAdapter
                adapter.updateData(filteredTransactions)
            } else {
                binding.textTotalSpending.text = "No supermarket spending found in the last 6 months"
                (binding.recyclerSupermarkets.adapter as TopRecipientAdapter).updateData(emptyList())
            }
        }
    }

    private fun loadData() {
        val sixMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -6)
        }.timeInMillis

        viewModel.loadSupermarketTransactions(sixMonthsAgo)

    }

    private fun formatCurrency(amount: Double): String {
        return CurrencyUtils.formatAmount(this, amount)
    }

}
