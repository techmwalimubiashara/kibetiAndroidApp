package com.mb.kibeti.retrofit_package

import com.mb.kibeti.budget.request.CurrentBudgetRequestBody
import com.mb.kibeti.budget.request.EditBudgetRequestBody
import com.mb.kibeti.budget.response.CurrentBudgetResponse
import com.mb.kibeti.budget.response.EditBudgetResponse
import com.mb.kibeti.smsReaderAutoTask.requests.ConfirmAllocatedTransactionsRequestBody
import com.mb.kibeti.smsReaderAutoTask.requests.DataBaseSearchRequestBody
import com.mb.kibeti.smsReaderAutoTask.requests.GetAutoAllocatedTransactionsRequestBody
import com.mb.kibeti.smsReaderAutoTask.requests.UpdateTransactionRequestBody
import com.mb.kibeti.smsReaderAutoTask.responses.ConfirmAllocatedTransactionsResponse
import com.mb.kibeti.smsReaderAutoTask.responses.DbSearchApiResponse
import com.mb.kibeti.smsReaderAutoTask.responses.GetAutoAllocatedTransactionsApiResponse
import com.mb.kibeti.smsReaderAutoTask.responses.UpdateTransactionApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("app_developer/outflow_budget.php")
    suspend fun getBudget(@Body currentBudgetRequestBody: CurrentBudgetRequestBody): Response<CurrentBudgetResponse>

    @POST("app_developer/outflow_budget.php")
    suspend fun updateEditedBudget(@Body editBudgetRequestBody: EditBudgetRequestBody):Response<EditBudgetResponse>

    @POST("app_developer/mpesa_search.php")
    suspend fun performDbSearch(@Body dataBaseSearchRequestBody: DataBaseSearchRequestBody) : Response<DbSearchApiResponse>

    @POST("app_developer/mpesa_search.php")
    suspend fun updateTransaction(@Body updateTransactionRequestBody: UpdateTransactionRequestBody):Response<UpdateTransactionApiResponse>

    @POST("app_developer/mpesa_search.php")
    suspend fun getAutoAllocatedTransactions(@Body getAutoAllocatedTransactionsRequestBody: GetAutoAllocatedTransactionsRequestBody):Response<GetAutoAllocatedTransactionsApiResponse>

    @POST("app_developer/mpesa_search.php")
    suspend fun confirmAllocatedTransactions(@Body confirmAllocatedTransactionsRequestBody: ConfirmAllocatedTransactionsRequestBody):Response<ConfirmAllocatedTransactionsResponse>
}
