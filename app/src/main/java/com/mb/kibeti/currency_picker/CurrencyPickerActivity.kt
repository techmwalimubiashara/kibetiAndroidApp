package com.mb.kibeti.currency_picker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker
import com.mb.kibeti.LoginActivity.EMAIL
import com.mb.kibeti.R
import org.json.JSONObject
import com.mb.kibeti.LoginActivity.MY_PREFERENCES
import com.mb.kibeti.MainActivity
import com.mb.kibeti.feedback.retrofit.RetrofitInstance

class CurrencyPickerActivity : AppCompatActivity() {

    private lateinit var countryCodePicker: CountryCodePicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_picker)

        countryCodePicker = findViewById(R.id.countryCodePicker)

        // Confirm Button
        val confirmButton: Button = findViewById(R.id.confirmButton)

        val notNowTv: TextView = findViewById(R.id.notNowBtn);
        confirmButton.setOnClickListener {
            val selectedCountryCode = countryCodePicker.selectedCountryNameCode
            val selectedCurrencyCode = getCurrencyCode(this, selectedCountryCode)

            saveCurrencyToPreferences(selectedCurrencyCode,selectedCountryCode.toString())
            logSavedCurrency()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Start the second Activity
            finish()
        }
        notNowTv.setOnClickListener {
            saveCurrencyToPreferences("KES","KE")
            logSavedCurrency()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Start the second Activity
            finish()
        }
    }


    private fun getCurrencyCode(context: Context, countryCode: String): String {
        val countryCurrencyMap = loadCurrencyMapping(context)
        return countryCurrencyMap[countryCode] ?: "KE" // Default to KE if country not found
    }


    private fun loadCurrencyMapping(context: Context): Map<String, String> {
        val jsonString = context.assets.open("country_currency.json")
            .bufferedReader()
            .use { it.readText() }
        val jsonObject = JSONObject(jsonString)

        // Convert JSON object to a Kotlin map
        val map = mutableMapOf<String, String>()
        jsonObject.keys().forEach { key ->
            map[key] = jsonObject.getString(key)
        }
        return map
    }


    private fun saveCurrencyToPreferences(currencyCode: String,countryCode: String) {

        val apiService = RetrofitInstance.currencyApi
        val sharedPrefs: SharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)

        val email = sharedPrefs.getString(EMAIL, "").toString()

        val postCurrency = PostCurrency(
            count_Code = countryCode,
            currency = currencyCode,
            email = email
        )

        apiService.postCurrency(postCurrency).enqueue(object : Callback<PostCurrency> {
            override fun onResponse(call: Call<PostCurrency>, response: Response<PostCurrency>) {
                if (response.isSuccessful) {
                    Log.e("Data posted to currency","Data posted successfully: ${response.body()}")
                } else {
                    Log.e("Failed to post to currency","Failed to post data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PostCurrency>, t: Throwable) {
                Log.e("Failed to call post","Error occurred: ${t.message}")
            }
        })


        sharedPrefs.edit().putString("currency", currencyCode).apply()

        // Log the value inakuwa saved
        Log.d("CurrencyPickerActivity", "Saved Currency: $currencyCode")
    }

    private fun logSavedCurrency() {
        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedCurrency = sharedPrefs.getString("currency", "KES") ?: "KES"

        // Log the saved value
        Log.d("CurrencyPickerActivity", "Confirmed Saved Currency: $savedCurrency")
    }
}
