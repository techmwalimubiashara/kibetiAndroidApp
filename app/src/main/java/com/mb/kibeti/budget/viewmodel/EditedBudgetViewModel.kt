package com.mb.kibeti.budget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.budget.repository.CurrentBudgetRepo
import com.mb.kibeti.budget.repository.EditedBudgetRepo
import com.mb.kibeti.budget.response.EditBudgetResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class EditedBudgetViewModel(val repo : EditedBudgetRepo): ViewModel() {

    //observing the live data from the backend and making it ready to be used in the frontend(view)
    private val _editedBudegetResult = MutableLiveData<Response<EditBudgetResponse>>()
    val editedBudegetResult : LiveData<Response<EditBudgetResponse>>get() = _editedBudegetResult


    //observing the error from the backend and making it ready to be displayed on the view
    private val _editedBudegetError = MutableLiveData<Throwable>()
    val editedBudegetError : LiveData<Throwable>get() = _editedBudegetError


    // view model function to help hit the endpoint
    fun saveEditedBudget(){
        viewModelScope.launch {
            try {
                val response = repo.saveEditedBudget()
                if (response.isSuccessful){
                    _editedBudegetResult.value = response
                }else{
                    _editedBudegetError.value = Throwable("Error: ${response.code()} ${response.message()}")
                }
            }catch (e:Exception){
                _editedBudegetError.value = e
            }
        }
    }
}

//viewmodel provider to be used to link the viewmodel to the UI (view)
class EditedBudgetViewModelFactory(private val repo: EditedBudgetRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditedBudgetViewModel::class.java)) {
            return EditedBudgetViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
