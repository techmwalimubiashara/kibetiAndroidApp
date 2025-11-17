package com.mb.kibeti.invest_guide

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.R
import java.text.DecimalFormat
import java.text.NumberFormat

class InvestmentAdapter(
    private val investments: List<Investment>,
    private val onInfoClicked: (String) -> Unit,
    private val onAmountChanged: (Int, Float) -> Unit
) : RecyclerView.Adapter<InvestmentAdapter.InvestmentViewHolder>() {

    inner class InvestmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvInvestmentName: TextView = itemView.findViewById(R.id.tvInvest01)
        private val imInvestmentInfo: ImageView = itemView.findViewById(R.id.imInvest1)
        private val etAmountInput: EditText = itemView.findViewById(R.id.etAmountInput)

        private var textWatcher: TextWatcher? = null
        private val numberFormat: NumberFormat = DecimalFormat("#,###.##")

        fun bind(investment: Investment, position: Int) {
            tvInvestmentName.text = investment.name

            imInvestmentInfo.setOnClickListener {
                onInfoClicked(investment.info)
            }

            textWatcher?.let { etAmountInput.removeTextChangedListener(it) }

            textWatcher = object : TextWatcher {
                private var current = ""

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val input = s.toString()

                    if (input == current) return

                    etAmountInput.removeTextChangedListener(this)

                    // Remove commas and keep only digits and decimal point
                    val cleanString = input.replace(",", "").replace(Regex("[^\\d.]"), "")

                    // Avoid multiple decimals
                    val sanitized = cleanString.split(".").let {
                        if (it.size > 2) it[0] + "." + it[1] else cleanString
                    }

                    val parsed = sanitized.toFloatOrNull() ?: 0f
                    investments[position].amount = parsed
                    onAmountChanged(position, parsed)

                    current = numberFormat.format(parsed)
                    etAmountInput.setText(current)
                    etAmountInput.setSelection(current.length)

                    etAmountInput.addTextChangedListener(this)
                }
            }.also {
                etAmountInput.addTextChangedListener(it)
            }

            etAmountInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    (itemView.parent as? RecyclerView)?.smoothScrollToPosition(position)
                }
            }

            // Set formatted value on bind
            val formatted = investment.amount?.let { numberFormat.format(it) } ?: ""
            etAmountInput.setText(formatted)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_investment_item, parent, false)
        return InvestmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvestmentViewHolder, position: Int) {
        holder.bind(investments[position], position)
    }

    override fun getItemCount() = investments.size
}
