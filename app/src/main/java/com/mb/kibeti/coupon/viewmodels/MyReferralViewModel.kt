package com.mb.kibeti.coupon.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.coupon.repos.ReferralsRepo
import com.mb.kibeti.coupon.responses.MyReferralsApiResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MyReferralViewModel(val repo : ReferralsRepo) : ViewModel() {
    private val  _referralResult = MutableLiveData<Response<MyReferralsApiResponse>>()
    val referralResult : LiveData<Response<MyReferralsApiResponse>> get() = _referralResult
    private val  _referralError = MutableLiveData<Throwable>()
    val referralError : LiveData<Throwable> get() = _referralError

    init {
        getMyReferrals()
    }

    private fun getMyReferrals() {
        viewModelScope.launch {
            try {
                val response = repo.getMyReferrals()
                if (response.isSuccessful){
                    _referralResult.value = response
                }else{
                    _referralError.value = Throwable("Error: ${response.code()} ${response.message()}")
                }
            }catch (e:Exception){
                _referralError.value = e
            }
        }
    }

}

class MyReferralViewModelFactory(private val repo: ReferralsRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyReferralViewModel::class.java)) {
            return MyReferralViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}