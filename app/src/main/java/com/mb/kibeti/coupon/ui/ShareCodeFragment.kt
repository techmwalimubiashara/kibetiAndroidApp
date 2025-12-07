package com.mb.kibeti.coupon.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mb.kibeti.R
import com.mb.kibeti.coupon.adapters.MyReferralsListAdapter
import com.mb.kibeti.coupon.repos.FetchRefferalCodeRepo
import com.mb.kibeti.coupon.repos.ReferralsRepo
import com.mb.kibeti.coupon.responses.MyReferralsApiResponseItem
import com.mb.kibeti.coupon.viewmodels.FetchRefferalViewModel
import com.mb.kibeti.coupon.viewmodels.FetchRefferalViewModelFactory
import com.mb.kibeti.coupon.viewmodels.MyReferralViewModel
import com.mb.kibeti.coupon.viewmodels.MyReferralViewModelFactory
import com.mb.kibeti.databinding.FragmentShareCodeBinding
import com.mb.kibeti.utils.EMAIL
import com.mb.kibeti.utils.PREFERENCES
import com.mb.kibeti.utils.REFERRAL_CODE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ShareCodeFragment : BottomSheetDialogFragment() {
    private lateinit var pref : SharedPreferences

    private lateinit var bind: FragmentShareCodeBinding
    private var fullReferralList: List<MyReferralsApiResponseItem> = emptyList()


    @SuppressLint("ServiceCast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and bind the view
        bind = FragmentShareCodeBinding.inflate(inflater, container, false)
        bind.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet)
        pref = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        val referralCode = pref.getString(REFERRAL_CODE,"")
        Log.d("REfCode", referralCode!!)

        val email = pref.getString(EMAIL, "")
        Log.d("email", email!!)


        bind.codeEditText.setText(referralCode)
        

        bind.closeButton.setOnClickListener {
            dismiss()
        }


        // Initialize the ViewModel
        val referralViewModel by viewModels<MyReferralViewModel> {
            MyReferralViewModelFactory(ReferralsRepo(email!!))
        }



        referralViewModel.referralResult.observe(viewLifecycleOwner, Observer{myreferral ->
            val body = myreferral.body()

            if (myreferral.isSuccessful && body != null){
                fullReferralList = myreferral.body()!!
                showFirstFive(fullReferralList)
            } else{
                Toast.makeText(requireContext(), "referrals not loaded", Toast.LENGTH_SHORT).show()
            }
        })

        // Handle the copy button click
        bind.codeEditText.setOnClickListener {
            val referralCode = bind.codeEditText.text.toString()
            if (referralCode.isNotEmpty()) {
                // Get the ClipboardManager system service
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Referral Code", referralCode)
                clipboard.setPrimaryClip(clip)
                // Show a Toast message indicating that the code was copied
                Toast.makeText(requireContext(), "Referral code copied to clipboard", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Referral code is empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle the share button click
        bind.shareButton.setOnClickListener {
            val referralCode = bind.codeEditText.text.toString()
            if (referralCode.isNotEmpty()) {
                val shareMessage = """
            🚀 Join me on the Kibeti App!
            Use my referral code: $referralCode to enjoy great discounts!.
            
            📲 Download the app from the Play Store:
            https://play.google.com/store/apps/details?id=com.mb.kibeti
        """.trimIndent()

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                    type = "text/plain"
                }

                // Start the share intent
                startActivity(Intent.createChooser(shareIntent, "Share via"))
                parentFragmentManager.beginTransaction().remove(this@ShareCodeFragment).commit()

            } else {
                Toast.makeText(requireContext(), "Referral code is empty", Toast.LENGTH_SHORT).show()


            }
        }

        // Return the root view of the binding
        return bind.root
    }

    private fun showFirstFive( referrals: List<MyReferralsApiResponseItem>) {
        val topFive = referrals.take(5)
        val  adapter = MyReferralsListAdapter(topFive)
        bind.referralsRecyclerView.adapter = adapter
        bind.referralsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        bind.secondLL.visibility = View.VISIBLE

        bind.noReferralsText.visibility = if (referrals.isEmpty()) View.VISIBLE else View.GONE

        // Show View All only if there are more than 5
        bind.viewAllButton.visibility = if (referrals.size > 5) View.VISIBLE else View.GONE

    }}