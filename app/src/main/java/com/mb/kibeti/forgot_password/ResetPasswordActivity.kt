package com.mb.kibeti.forgot_password


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.R

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        supportActionBar?.hide()

    }
}
