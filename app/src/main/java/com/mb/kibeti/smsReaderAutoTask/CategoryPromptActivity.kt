package com.mb.kibeti.smsReaderAutoTask

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mb.kibeti.R
import com.mb.kibeti.databinding.ActivityCategoryPromptBinding

class CategoryPromptActivity : AppCompatActivity() {
    private lateinit var bind: ActivityCategoryPromptBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCategoryPromptBinding.inflate(layoutInflater)
        setContentView(bind.root)


//retrieve data from intent
        val recipientName = intent.getStringExtra("recipientName")
        val amount = intent.getStringExtra("amount")
        val date = intent.getStringExtra("date")

        //bind data
        bind.tvRecipientName.text = "Recipient: $recipientName"
        bind.tvAmount.text = "Amount: $amount"
        bind.tvDate.text = "Date: $date"

// get category from edit text


        //handle submitbutton click
        bind.btnSubmitCategory.setOnClickListener {
            val category = bind.etCategory.text.toString().trim()
            if (category.isEmpty()) {
                Toast.makeText(this, "please enter category", Toast.LENGTH_SHORT).show()
            } else {
                DbHandlerMock.addCategoryForRecipient(recipientName ?: "", category)
                Toast.makeText(this, "category added successful", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}