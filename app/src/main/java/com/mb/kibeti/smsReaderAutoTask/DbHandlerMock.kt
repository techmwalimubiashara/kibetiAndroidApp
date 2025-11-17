package com.mb.kibeti.smsReaderAutoTask

object DbHandlerMock  {


        // This would act like a mock database storing recipient-category pairs
        private val recipientCategoryMap: MutableMap<String, String> = mutableMapOf(
            "Naivas" to "Groceries",
            "Total Petrol" to "Fuel",
            "Netflix" to "Entertainment"
        )

        // This function retrieves the category for a recipient
        fun getCategoryForRecipient(recipient: String): String? {
            return recipientCategoryMap[recipient]
        }

        // This function adds a new recipient with a category
        fun addCategoryForRecipient(recipient: String, category: String) {
            recipientCategoryMap[recipient] = category
        }

        // Function to add an expenditure (in a real app, this would save to a database)
        fun addExpenditure(recipient: String, amount: String, category: String, date: String) {
            // Save the expenditure somewhere (just printing here for now)
            println("Expenditure added: Recipient=$recipient, Amount=$amount, Category=$category")
        }
    }

