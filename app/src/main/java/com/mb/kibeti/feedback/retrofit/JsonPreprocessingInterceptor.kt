//package com.example.plannerapp.feedback.retrofit
//
//import okhttp3.Interceptor
//import okhttp3.Response
//import okhttp3.ResponseBody
//import java.io.IOException
//
//class JsonPreprocessingInterceptor : Interceptor {
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        // Proceed with the request
//        val originalResponse = chain.proceed(chain.request())
//
//        // Get the original response body
//        val originalResponseBody = originalResponse.body?.source()?.buffer()?.readUtf8() ?: ""
//
//        // Preprocess the response body
//        val cleanedResponseBody = preprocessResponse(originalResponseBody)
//
//        // Create a new response body
//        val newResponseBody = ResponseBody.create(originalResponse.body?.contentType(), cleanedResponseBody)
//
//        // Return the new response with the cleaned body
//        return originalResponse.newBuilder()
//            .body(newResponseBody)
//            .build()
//    }
//
//    private fun preprocessResponse(responseBody: String): String {
//        // Example: Remove unexpected characters or text
//        return responseBody.replace(Regex("?>$"), "")
//    }
//}
//
//
