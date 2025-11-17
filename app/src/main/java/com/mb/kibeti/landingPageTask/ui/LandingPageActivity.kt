package com.mb.kibeti.landingPageTask.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.LoginActivity
import com.mb.kibeti.LoginActivity.INTRO
import com.mb.kibeti.databinding.ActivityLandingPageBinding

class LandingPageActivity : AppCompatActivity() {
    private lateinit var bind: ActivityLandingPageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(bind.root)


        // make the activity on full screen
        sharedPreferences =
            applicationContext.getSharedPreferences(LoginActivity.MY_PREFERENCES, MODE_PRIVATE)


        // when this activity is about to be launch we need to check if its openened before or not
        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, LoginActivity::class.java)
            startActivity(mainActivity)
            finish()
        }


        bind.tryForFreeButton.setOnClickListener {
            val intent = Intent(this, FinanceFeelingActivity::class.java)
            startActivity(intent)
            finish()
        }
        bind.haveAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
//            savePrefsData()
            startActivity(intent)
            finish()

//           val intent1 = Intent(this, MainActivity::class.java)
//           startActivity(intent1) // Start the second Activity
        }

    }

    fun restorePrefData(): Boolean {
        return sharedPreferences.getBoolean(INTRO, false)
    }

    private fun savePrefsData() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.commit()
    }
}