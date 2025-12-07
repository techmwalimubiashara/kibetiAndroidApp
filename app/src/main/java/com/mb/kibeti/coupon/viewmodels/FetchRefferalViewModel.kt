package com.mb.kibeti.coupon.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.coupon.repos.FetchRefferalCodeRepo
import com.mb.kibeti.coupon.responses.FetchRefferalCodeResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class FetchRefferalViewModel(val repo : FetchRefferalCodeRepo): ViewModel(){
    private  val _fetchCodeResult = MutableLiveData<Response<FetchRefferalCodeResponse>>()
    val fetchCodeResult : LiveData<Response<FetchRefferalCodeResponse>>
        get() = _fetchCodeResult

    private  val _fetchCodeError = MutableLiveData<Throwable>()
    val fetchCodeError : LiveData<Throwable>
        get() = _fetchCodeError

     fun fetchCode() {
        viewModelScope.launch {
            try {
                val response = repo.fetchRefferalCode()
                if (response.isSuccessful){
                    _fetchCodeResult.value = response
                }else{
                    _fetchCodeError.value = Throwable("Error: ${response.code()} ${response.message()}")
                }
            }catch (e:Exception){
                _fetchCodeError.value = e

            }
        }

    }
}

class FetchRefferalViewModelFactory(private val repo: FetchRefferalCodeRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FetchRefferalViewModel::class.java)) {
            return FetchRefferalViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}