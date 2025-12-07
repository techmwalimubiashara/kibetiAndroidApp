package com.mb.kibeti.coupon.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mb.kibeti.R
import com.mb.kibeti.coupon.adapters.MyWalletAdapter
import com.mb.kibeti.coupon.repos.MyWalletRepo
import com.mb.kibeti.coupon.viewmodels.MyWalletViewModel
import com.mb.kibeti.coupon.viewmodels.MyWalletViewModelFactory
import com.mb.kibeti.databinding.ActivityMyWalletBinding
import com.mb.kibeti.utils.EMAIL
import com.mb.kibeti.utils.PREFERENCES

class MyWalletActivity : AppCompatActivity() {

    private lateinit var bind : ActivityMyWalletBinding
    private lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMyWalletBinding.inflate(layoutInflater)
        setContentView(bind.root)

        pref = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val email = pref.getString(EMAIL, "")


        val viewModel by viewModels<MyWalletViewModel> {
            MyWalletViewModelFactory(MyWalletRepo(email!!))
        }

        viewModel.myWalletResult.observe(this, Observer {result ->
            if (result.isSuccessful && result.body() != null){
                bind.textViewBalance.text = result.body()!!.balance.toFloat().toInt().toString()

                val transactionList = result.body()!!.transactions
                val adapter = MyWalletAdapter(transactionList)
                bind.recyclerViewTransactions.layoutManager = LinearLayoutManager(this)

                bind.recyclerViewTransactions.adapter = adapter

            }else{
                Toast.makeText(this, "Error fetching list",Toast.LENGTH_SHORT ).show()
            }

        })
    }
}