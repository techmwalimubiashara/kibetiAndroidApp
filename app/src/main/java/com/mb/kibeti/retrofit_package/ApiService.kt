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
import com.mb.kibeti.coupon.requests.FetchReferralCodeRequestBody
import com.mb.kibeti.coupon.requests.GenerateReferralCodeRequestBody
import com.mb.kibeti.coupon.requests.MyReferralsRequestBody
import com.mb.kibeti.coupon.requests.MyWalletRequestBody
import com.mb.kibeti.coupon.requests.SampleLoginRequest
import com.mb.kibeti.coupon.responses.FetchRefferalCodeResponse
import com.mb.kibeti.coupon.responses.GetRefferalCodeResponse
import com.mb.kibeti.coupon.responses.MyReferralsApiResponse
import com.mb.kibeti.coupon.responses.MyWalletApiResponse
import com.mb.kibeti.coupon.responses.SampleLoginApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    @POST("generate-referral-code")
    suspend fun generateRefferalCode(@Body generateReferralCodeRequestBody: GenerateReferralCodeRequestBody): Response <GetRefferalCodeResponse>

    @POST("login")
    suspend fun sampleLogin(@Body sampleLoginRequest: SampleLoginRequest): Response<SampleLoginApiResponse>

    @GET("my-referral-code")
    suspend fun fetchRefferalCode(@Query("email") email: String): Response<FetchRefferalCodeResponse>

    @GET("referrals")
    suspend fun getMyReferrals(@Query("email") email: String) : Response<MyReferralsApiResponse>

    @GET("wallet")
    suspend fun getMyWallet(@Body myWalletRequestBody: MyWalletRequestBody) : Response<MyWalletApiResponse>
}
