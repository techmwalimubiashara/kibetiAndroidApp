package com.mb.kibeti.landingPageTask.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mb.kibeti.LoginActivity
import com.mb.kibeti.Makepayment
import com.mb.kibeti.R

class PaymentActivity : AppCompatActivity() {
    private lateinit var btnPayment: Button
    private lateinit var btnBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get data from the intent
        val receivedData = intent.getStringExtra("from_activity")

        setContentView(R.layout.activity_payment)
        if(receivedData.equals("investment")){
            setContentView(R.layout.activity_payment2)
        }

        btnPayment = findViewById(R.id.btnMakePayment)
        btnBack = findViewById(R.id.arrow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnPayment.setOnClickListener {
            val intent = Intent(this, Makepayment::class.java)
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
//            savePrefsData()
            startActivity(intent)
            finish()
        }
        btnBack.setOnClickListener{
            finish()
        }
    }
}