package com.mb.kibeti.coupon.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.coupon.repos.GenerateRefferalCodeRepo
import com.mb.kibeti.coupon.responses.GetRefferalCodeResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class GenerateRefferalCodeViewModel (val repo: GenerateRefferalCodeRepo) : ViewModel(){
    private val _getRefferalCodeResults = MutableLiveData<Response<GetRefferalCodeResponse>>()
    val getRefferalCodeResults: LiveData<Response<GetRefferalCodeResponse>> get() = _getRefferalCodeResults


    private val _getRefferalCodeError = MutableLiveData<Throwable>()
    val getRefferalCodeError: LiveData<Throwable> get() = _getRefferalCodeError


    fun generateRefferalCode() {
        viewModelScope.launch {
            try {
                val response = repo.generateCode()

                _getRefferalCodeResults.value = response

            } catch (e: Exception) {
                _getRefferalCodeError.value = e
            }
        }
    }

}

class GenerateRefferalCodeViewModelFactory(private val repo: GenerateRefferalCodeRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenerateRefferalCodeViewModel::class.java)) {
            return GenerateRefferalCodeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}