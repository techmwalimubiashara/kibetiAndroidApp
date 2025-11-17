package com.mb.kibeti.budget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.budget.repository.CurrentBudgetRepo
import com.mb.kibeti.budget.response.CurrentBudgetResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class CurrentBudgetViewModel(val repo : CurrentBudgetRepo): ViewModel() {
    private val _currentBalanceResult = MutableLiveData<Response<CurrentBudgetResponse>>()
    val currentBalanceResult : LiveData<Response<CurrentBudgetResponse>>get() = _currentBalanceResult


    private val _currentBalanceError = MutableLiveData<Throwable>()
    val currentBalanceError : LiveData<Throwable>get() = _currentBalanceError


    init {
        getCurrentBudget()
    }

    private fun getCurrentBudget() {
        viewModelScope.launch {
            try {
                val response = repo.getCurrentBudget()
                if (response.isSuccessful){
                    _currentBalanceResult.value = response
                }else{
                    _currentBalanceError.value = Throwable("Error: ${response.code()} ${response.message()}")
                }

            }catch (e:Exception){
                _currentBalanceError.value = e
            }
        }
    }

}


class CurrentBudgetViewModelFactory(private val repo: CurrentBudgetRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentBudgetViewModel::class.java)) {
            return CurrentBudgetViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
