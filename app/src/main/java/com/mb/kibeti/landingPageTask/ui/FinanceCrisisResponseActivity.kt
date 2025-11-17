package com.mb.kibeti.landingPageTask.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.LoginActivity
import com.mb.kibeti.R
import com.mb.kibeti.RegisterActivity
import com.mb.kibeti.databinding.ActivityFinanceResponseBinding

class FinanceCrisisResponseActivity : AppCompatActivity() {
    private lateinit var bind : ActivityFinanceResponseBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      bind = ActivityFinanceResponseBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()

        val btn_lets_go = findViewById<Button>(R.id.btn_lets_go)

        bind.arrow.setOnClickListener { finish() }


        btn_lets_go.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
