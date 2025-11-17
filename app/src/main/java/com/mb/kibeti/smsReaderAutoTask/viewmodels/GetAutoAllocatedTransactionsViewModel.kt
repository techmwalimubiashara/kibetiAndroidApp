package com.mb.kibeti.smsReaderAutoTask.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.budget.repository.CurrentBudgetRepo
import com.mb.kibeti.budget.viewmodel.CurrentBudgetViewModel
import com.mb.kibeti.smsReaderAutoTask.repositories.GetAutoAllocatedTransactionsRepo
import com.mb.kibeti.smsReaderAutoTask.responses.GetAutoAllocatedTransactionsApiResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class GetAutoAllocatedTransactionsViewModel(val repo : GetAutoAllocatedTransactionsRepo) : ViewModel() {
    private val _autoAllocateTransactionsResults = MutableLiveData<Response<GetAutoAllocatedTransactionsApiResponse>>()
    val autoAllocateTransactionsResults : LiveData<Response<GetAutoAllocatedTransactionsApiResponse>>get() = _autoAllocateTransactionsResults

    private val _autoAllocateTransactionsError = MutableLiveData<Throwable>()
    val autoAllocateTransactionsError : LiveData<Throwable>get() = _autoAllocateTransactionsError

    init {
        getAutoAllocatedTransactionsInsideViewModel()
    }

    private fun getAutoAllocatedTransactionsInsideViewModel() {
        viewModelScope.launch {
            try {
                val response = repo.getAutoAllocatedTransactionsInsideRepo()
                Log.d("excecuting", "excecuting")
                if (response.isSuccessful){
                    Log.d("responseadmin",response.body().toString())
                    Log.d("response","success")

                    _autoAllocateTransactionsResults.value = response

                }else{
                    _autoAllocateTransactionsError.value = Throwable("Error : ${response.code()} ${response.message()}")
                }
            }catch (e:Exception){
                _autoAllocateTransactionsError.value = e
                Log.d("current error", e.toString())
            }
        }
    }


}

class GetAutoAllocatedTransactionsViewModelFactory(private val repo: GetAutoAllocatedTransactionsRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetAutoAllocatedTransactionsViewModel::class.java)) {
            return GetAutoAllocatedTransactionsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
