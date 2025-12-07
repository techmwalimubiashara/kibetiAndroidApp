package com.mb.kibeti.coupon.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.coupon.repos.SampleLoginRepo
import com.mb.kibeti.coupon.responses.SampleLoginApiResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class SampleLoginViewModel (val repo : SampleLoginRepo): ViewModel() {

    private val _loginresult = MutableLiveData<Response<SampleLoginApiResponse>>()
    val loginResult : LiveData<Response<SampleLoginApiResponse>> get() = _loginresult

    private val _loginrErrror = MutableLiveData<Throwable>()
    val loginError : LiveData<Throwable> get() = _loginrErrror

    fun login(){
        viewModelScope.launch {
            try {
                val response = repo.login()
                if (response.isSuccessful){
                    _loginresult.value = response
                }else{
                    _loginrErrror.value = Throwable("Error: ${response.code()}, ${response.message()}")
                }
            }catch (e:Exception){
                _loginrErrror.value = e
            }
        }
    }

}class SampleLoginViewModelFactory(private val repo: SampleLoginRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SampleLoginViewModel::class.java)) {
            return SampleLoginViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}