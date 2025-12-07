package com.mb.kibeti.coupon.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.mb.kibeti.R
import com.mb.kibeti.coupon.repos.FetchRefferalCodeRepo
import com.mb.kibeti.coupon.repos.GenerateRefferalCodeRepo
import com.mb.kibeti.coupon.viewmodels.FetchRefferalViewModel
import com.mb.kibeti.coupon.viewmodels.FetchRefferalViewModelFactory
import com.mb.kibeti.coupon.viewmodels.GenerateRefferalCodeViewModel
import com.mb.kibeti.coupon.viewmodels.GenerateRefferalCodeViewModelFactory
import com.mb.kibeti.databinding.ActivityInviteFriendBinding
import com.mb.kibeti.utils.EMAIL
import com.mb.kibeti.utils.PREFERENCES
import com.mb.kibeti.utils.REFERRAL_CODE

class InviteFriendActivity : AppCompatActivity() {
    private lateinit var pref :SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var bind: ActivityInviteFriendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityInviteFriendBinding.inflate(layoutInflater)
        setContentView(bind.root)

        pref = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        editor = pref.edit()
        val email = pref.getString(EMAIL, "")

        val fetchCodeViewModel by viewModels<FetchRefferalViewModel> {
            FetchRefferalViewModelFactory(FetchRefferalCodeRepo(email!!))
        }

        val viewModel by viewModels<GenerateRefferalCodeViewModel> {
            GenerateRefferalCodeViewModelFactory(GenerateRefferalCodeRepo(email!!))
        }

        bind.generateCodeBTN.setOnClickListener {
            viewModel.generateRefferalCode()

        }

//        bind.mywallet.setOnClickListener {
//            val intent = Intent(this, MyWalletActivity::class.java)
//            startActivity(intent)
//        }

        bind.inviteAfriendBTN.setOnClickListener {
            fetchCodeViewModel.fetchCode()
        }

        fetchCodeViewModel.fetchCodeResult.observe(this, Observer { result ->
            if (result.isSuccessful) {
                val referralCode = result.body()?.referral_code

                if (referralCode != null) {
                    // ✅ Code exists — save and show
                    editor.putString(REFERRAL_CODE, referralCode).apply()
                    showCodeFragment()
                } else {
                    // ⚙️ Code not found — automatically generate it
                    Log.d("InviteFriendActivity", "Referral code not found. Generating a new one...")

                    // Generate a new referral code
                    viewModel.generateRefferalCode()

                    // Observe generation result
                    viewModel.getRefferalCodeResults.observe(this, Observer { genResult ->
                        if (genResult != null && genResult.isSuccessful) {
                            Log.d("InviteFriendActivity", "Referral code generated successfully. Refetching...")

                            // ✅ Once generated, refetch to get the new code
                            fetchCodeViewModel.fetchCode()

                        } else {
                            Toast.makeText(
                                this,
                                "Failed to generate referral code. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            } else {
                Toast.makeText(this, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
            }
        })

//        viewModel.getRefferalCodeResults.observe(this, Observer { result ->
//            if (result != null &&  result.isSuccessful) {
//                Toast.makeText(
//                    this,
//                    "${result.message()}\nYour code is: ${result.body()!!.referral_code}\nClick invite button to share it.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else if (result != null && result.code() == 400) {
//                Toast.makeText(
//                    this, "Referral code already exists.\nClick invite button to share it.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }else{
//                Toast.makeText(this, "result is null",Toast.LENGTH_SHORT).show()
//            }
//
//
//        })
//        viewModel.getRefferalCodeError .observe(this) { error ->
//            Toast.makeText(this, "${error}", Toast.LENGTH_LONG).show()
//        }

    }

    private fun showCodeFragment() {
        val bottomSheet = ShareCodeFragment()
        bottomSheet.show(supportFragmentManager, "ShareCodeBottomSheet")
    }

}