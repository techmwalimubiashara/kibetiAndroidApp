package com.mb.kibeti.budget.repository

import com.mb.kibeti.budget.request.EditBudgetRequestBody
import com.mb.kibeti.budget.response.EditBudgetResponse
import com.mb.kibeti.retrofit_package.RetrofitClient
import retrofit2.Response

/**
 * EditedBudgetRepo class is responsible for handling the editing of a budget.
 * It interacts with the API service to save the updated budget data.
 */
class EditedBudgetRepo(
    private val email: String,
    private val action: String,
    private val month: Int,
    private val year: Int,
    private val day: Int,
    private val dependents: Int,
    private val savings: Int,
    private val investments: Int,
    private val mobility: Int,
    private val giving: Int,
    private val self: Int,
    private val loan: Int,
    private val protection: Int,
    private val others: Int
) {
    // Retrofit service instance used to communicate with the backend API.
    private val retrofit = RetrofitClient.apiService

    /**
     * This function sends the edited budget data to the backend API for saving.
     *
     * @return A Response object containing the result of the API call. This could be
     *         successful (with the updated budget data) or unsuccessful (with an error).
     */
    suspend fun saveEditedBudget(): Response<EditBudgetResponse> {
        // Create a request body containing all the necessary fields for the API call.
        val requestBody = EditBudgetRequestBody(
            email,
            action,
            month,
            year,
            day,
            dependents,
            savings,
            investments,
            mobility,
            giving,
            self,
            loan,
            protection,
            others
        )

        // Call the API's updateEditedBudget method, passing the request body.
        return retrofit.updateEditedBudget(requestBody)
    }
}
