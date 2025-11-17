package com.mb.kibeti.landingPageTask.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mb.kibeti.LoginActivity
import com.mb.kibeti.R
import com.mb.kibeti.RegisterActivity
import com.mb.kibeti.databinding.ActivityFinanceStressedResponseBinding
import com.mb.kibeti.databinding.ActivityFinanceThrivingResponseBinding

class FinanceThrivingResponseActivity : AppCompatActivity() {
    private lateinit var bind : ActivityFinanceThrivingResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityFinanceThrivingResponseBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.arrow.setOnClickListener { finish() }

        bind.btnLetsGo.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}