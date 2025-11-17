package com.mb.kibeti.sms_filter

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    private val _supermarketTransactions = MutableLiveData<List<RecipientSummary>>()
    val supermarketTransactions: LiveData<List<RecipientSummary>> = _supermarketTransactions
    val allTransactions: LiveData<List<TransactionEntity>>
    val topRecipients: LiveData<List<RecipientSummary>>
    val totalSpending: LiveData<Double>
    val last10Transactions: LiveData<List<TransactionEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)
        allTransactions = repository.getRecentTransactions()
        topRecipients = repository.getTopRecipients()
        totalSpending = repository.getTotalSpending()
        last10Transactions = repository.getLast10Transactions()
    }

    fun loadInitialData(context: Context) {
        viewModelScope.launch {
            repository.fetchExistingMpesaTransactions(context)
        }
    }

    fun loadSupermarketTransactions(sixMonthsAgo: Long) {
        viewModelScope.launch {
           // Log.d("Supermarket", "Loading transactions for: ${supermarketNames.joinToString()}")
            val transactions = repository.getSupermarketTransactions(sixMonthsAgo)
            Log.d("Supermarket", "Found ${transactions.size} transactions")
            _supermarketTransactions.postValue(transactions)
        }
    }

    fun refreshData(context: Context) {
        viewModelScope.launch {
            repository.clearAndReprocess(context)
        }
    }
}