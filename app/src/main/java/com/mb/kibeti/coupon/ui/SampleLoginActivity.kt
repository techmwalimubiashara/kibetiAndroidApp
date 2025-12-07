package com.mb.kibeti.coupon.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.mb.kibeti.R
import com.mb.kibeti.coupon.repos.SampleLoginRepo
import com.mb.kibeti.coupon.viewmodels.SampleLoginViewModel
import com.mb.kibeti.coupon.viewmodels.SampleLoginViewModelFactory
import com.mb.kibeti.databinding.ActivitySampleLogin2Binding
import com.mb.kibeti.retrofit_package.RetrofitClient
import com.mb.kibeti.utils.EMAIL
import com.mb.kibeti.utils.PREFERENCES

class SampleLoginActivity : AppCompatActivity() {
         private lateinit var bind : ActivitySampleLogin2Binding
        private lateinit var pref : SharedPreferences
        private lateinit var editor : SharedPreferences.Editor
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            bind = ActivitySampleLogin2Binding.inflate(layoutInflater)
            setContentView(bind.root)

            pref = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            editor = pref.edit()

            bind.loginBTN.setOnClickListener {
                val email = bind.email.text.toString()
                val password = bind.password.text.toString()

                val viewModel by viewModels<SampleLoginViewModel> {
                    SampleLoginViewModelFactory(SampleLoginRepo(email, password))
                }

                viewModel.login()


                viewModel.loginResult.observe(this, Observer { result ->
                    if(result.isSuccessful && result != null){
                        val email = result.body()!!.user.email
                        editor.putString(EMAIL, email).apply()
                        Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show()
                        RetrofitClient.accessToken = result.body()!!.token

                        val intent = Intent(this, InviteFriendActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Error: result not successiful", Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }
    }
