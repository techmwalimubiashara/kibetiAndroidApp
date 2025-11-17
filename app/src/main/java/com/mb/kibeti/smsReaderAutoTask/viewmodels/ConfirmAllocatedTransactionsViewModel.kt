package com.mb.kibeti.smsReaderAutoTask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.smsReaderAutoTask.repositories.ConfirmAllocatedTransactionsRepo
import com.mb.kibeti.smsReaderAutoTask.repositories.GetAutoAllocatedTransactionsRepo
import com.mb.kibeti.smsReaderAutoTask.responses.ConfirmAllocatedTransactionsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ConfirmAllocatedTransactionsViewModel(val repo : ConfirmAllocatedTransactionsRepo) : ViewModel(){
    private val _confirmAutoAllocatedTransactionsResult = MutableLiveData<Response<ConfirmAllocatedTransactionsResponse>>()
    val confirmAutoAllocatedTransactionsResult : LiveData<Response<ConfirmAllocatedTransactionsResponse>>get() = _confirmAutoAllocatedTransactionsResult


    private val _confirmAutoAllocatedTransactionsError = MutableLiveData<Throwable>()
    val confirmAutoAllocatedTransactionsError : LiveData<Throwable>get() = _confirmAutoAllocatedTransactionsError


     fun confirmAllocatedTransactions(){
        viewModelScope.launch {
            try {
                val response = repo.confirmAutoAllocatedTransactions()
                if (response.isSuccessful){
                    _confirmAutoAllocatedTransactionsResult.value = response
                }else{
                    _confirmAutoAllocatedTransactionsError.value = Throwable("Error: ${response.code()}, ${response.message()}")
                }

            }catch (e: Exception){

            }
        }
    }


}
class ConfirmAllocatedTransactionsViewModelFactory(private val repo: ConfirmAllocatedTransactionsRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfirmAllocatedTransactionsViewModel::class.java)) {
            return ConfirmAllocatedTransactionsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
