package com.mb.kibeti.landingPageTask.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mb.kibeti.R
import com.mb.kibeti.currency_picker.CurrencyPickerActivity
import com.mb.kibeti.databinding.ActivityWhatsYourNameBinding

class ActivityWhatsYourName : AppCompatActivity() {
    private lateinit var binding: ActivityWhatsYourNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityWhatsYourNameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.continueBtn.isEnabled = false
        binding.continueBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)


        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isNotEmpty = !s.isNullOrEmpty()

                binding.continueBtn.isEnabled = isNotEmpty
                val buttonColor = if (isNotEmpty) {
                    R.color.primary_orange
                } else {
                    R.color.grey
                }


                binding.continueBtn.backgroundTintList = ContextCompat.getColorStateList(this@ActivityWhatsYourName, buttonColor)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.continueBtn.setOnClickListener {
            val intent = Intent(this, FinanceFeelingActivity::class.java)
            startActivity(intent)
        }
    }
}
