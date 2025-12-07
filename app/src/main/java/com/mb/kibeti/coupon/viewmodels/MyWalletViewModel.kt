package com.mb.kibeti.coupon.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.coupon.repos.MyWalletRepo
import com.mb.kibeti.coupon.responses.MyWalletApiResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MyWalletViewModel(val repo : MyWalletRepo): ViewModel() {
    private val _myWalletResult = MutableLiveData<Response<MyWalletApiResponse>>()
    val myWalletResult : LiveData<Response<MyWalletApiResponse>> get() = _myWalletResult


    private val _myWalletError = MutableLiveData<Throwable>()
    val myWalletError : LiveData<Throwable> get() = _myWalletError

    init {
        getMyWallet()
    }

    private fun getMyWallet() {
        viewModelScope.launch {
            try {
                val response = repo.getMyWallet()
                if (response.isSuccessful){
                    _myWalletResult.value = response
                } else{
                    _myWalletError.value = Throwable("Error: ${response.code()} ${response.message()}")
                }
            }catch (e:Exception){
                _myWalletError.value = e
            }
        }
    }
}

class MyWalletViewModelFactory(private val repo: MyWalletRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyWalletViewModel::class.java)) {
            return MyWalletViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}