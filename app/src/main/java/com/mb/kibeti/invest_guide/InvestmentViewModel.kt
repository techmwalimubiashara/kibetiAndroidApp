package com.mb.kibeti.invest_guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InvestmentViewModel : ViewModel() {

    private val _availableAmount = MutableLiveData<Float>()
    val availableAmount: LiveData<Float> get() = _availableAmount

    private val enteredAmounts = mutableMapOf<Int, Float>()

    fun setAvailableAmount(amount: Float) {
        _availableAmount.value = amount
    }

    fun updateAmount(position: Int, newAmount: Float) {
        val previousAmount = enteredAmounts[position] ?: 0.0f
        val difference = newAmount - previousAmount

        enteredAmounts[position] = newAmount
        _availableAmount.value = (_availableAmount.value ?: 0.0f) - difference
    }

    fun hasExceededLimit(): Boolean {
        return _availableAmount.value?.let { it < 0 } == true
    }
}


