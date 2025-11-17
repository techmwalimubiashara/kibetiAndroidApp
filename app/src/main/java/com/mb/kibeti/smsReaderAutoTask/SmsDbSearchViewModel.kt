package com.mb.kibeti.smsReaderAutoTask
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.plannerapp.Budget.repository.EditedBudgetRepo
//import com.example.plannerapp.Budget.viewmodel.EditedBudgetViewModel
//import com.example.plannerapp.smsReaderAutoTask.responses.DbSearchApiResponse
//import kotlinx.coroutines.launch
//import retrofit2.Response
//
//class SmsDbSearchViewModel(val repo : SmsDbSearchRepo) : ViewModel() {
//    private val _dbSearchResult = MutableLiveData<Response<DbSearchApiResponse>>()
//    val dbSearchResult : LiveData<Response<DbSearchApiResponse>>get() = _dbSearchResult
//
//    private val _dbSearchError = MutableLiveData<Throwable>()
//    val dbSearchError : LiveData<Throwable>get() = _dbSearchError
//
//
//
//     fun performSmsDbSearch(action : String, search_cat: String, search_name: String, email:String) {
//        viewModelScope.launch {
//            try {
//
//
//
//                val response = repo.performSmsDbSearch(action, search_cat, search_name,email)
//                if (response.isSuccessful){
//                    _dbSearchResult.value = response
//                    Log.d("response", "${response.body()}")
//                }
//                else
//                {_dbSearchError.value = Throwable("Error: ${response.code()} ${response.message()}")
//                    Log.d("noresponse", "${response.message()}")
//
//                }
//            }catch (e:Exception){
//                _dbSearchError.value = e
//            }
//        }
//    }
//}
//
////viewmodel provider to be used to link the viewmodel to the smsReciever class.
//class SmsDbSearchViewModelFactory(private val repo: SmsDbSearchRepo) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SmsDbSearchViewModel::class.java)) {
//            return SmsDbSearchViewModel(repo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
