package com.mb.kibeti.feedback.screens

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mb.kibeti.LoginActivity
import com.mb.kibeti.R
import com.mb.kibeti.feedback.repository.FeedbackRepository
import com.mb.kibeti.feedback.retrofit.RetrofitInstance
import com.mb.kibeti.feedback.viewmodel.FeedbackViewModel
import com.mb.kibeti.feedback.viewmodel.FeedbackViewModelFactory

class FeedbackActivity : AppCompatActivity() {

    private lateinit var emojiVeryBad: TextView
    private lateinit var emojiBad: TextView
    private lateinit var emojiNeutral: TextView
    private lateinit var emojiGood: TextView
    private lateinit var emojiVeryGood: TextView
    private lateinit var categorySpinner: Spinner
    private lateinit var commentText: EditText
    private lateinit var backButton: ImageButton
    private lateinit var submitFeedbackButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var selectedRate: String = ""
    private var email: String = ""

    private val viewModel: FeedbackViewModel by viewModels {
        FeedbackViewModelFactory(FeedbackRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, MODE_PRIVATE)

        email = sharedPreferences.getString(LoginActivity.EMAIL, "").toString()
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        emojiVeryBad = findViewById(R.id.rate1)
        emojiBad = findViewById(R.id.rate2)
        emojiNeutral = findViewById(R.id.rate3)
        emojiGood = findViewById(R.id.rate4)
        emojiVeryGood = findViewById(R.id.rate5)
        categorySpinner = findViewById(R.id.spinnerCategory)
        commentText = findViewById(R.id.commentText)
        backButton = findViewById(R.id.backButton)
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton)

        setupSpinner()
    }

    private fun setupListeners() {
        emojiVeryBad.setOnClickListener {
            selectedRate = "Very Bad"
            highlightSelectedRate(emojiVeryBad)
        }
        emojiBad.setOnClickListener {
            selectedRate = "Bad"
            highlightSelectedRate(emojiBad)
        }
        emojiNeutral.setOnClickListener {
            selectedRate = "Neutral"
            highlightSelectedRate(emojiNeutral)
        }
        emojiGood.setOnClickListener {
            selectedRate = "Good"
            highlightSelectedRate(emojiGood)
        }
        emojiVeryGood.setOnClickListener {
            selectedRate = "Very Good"
            highlightSelectedRate(emojiVeryGood)
        }
        backButton.setOnClickListener{
            finish()
        }

        submitFeedbackButton.setOnClickListener {
            submitFeedback()
        }
    }

    private fun highlightSelectedRate(selectedEmoji: TextView) {
        emojiVeryBad.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        emojiBad.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        emojiNeutral.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        emojiGood.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        emojiVeryGood.setBackgroundColor(android.graphics.Color.TRANSPARENT)

        selectedEmoji.setBackgroundColor(android.graphics.Color.LTGRAY)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.feedback_categories,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        categorySpinner.adapter = adapter
    }

    private fun submitFeedback() {
        val selectedCategory = categorySpinner.selectedItem.toString()
        val comment = commentText.text.toString()

        if (selectedRate.isBlank() || selectedCategory.isBlank() || comment.isBlank()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.postFeedback(
            email = email,
            experience = selectedRate,
            category = selectedCategory,
            comment = comment,
            onSuccess = { response ->
//                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                showThankYouDialog()
            },
            onFailure = { errorMessage ->
//                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Failed to get response", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun showThankYouDialog() {
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_thank_you, null)
        dialog.setContentView(dialogView)

        dialogView.findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.goBackHomeButton).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.setCancelable(true)
        dialog.show()
    }
}