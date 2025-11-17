package com.mb.kibeti.sms_filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.R
import java.text.NumberFormat
import java.util.*

class TopRecipientAdapter(private var recipients: List<RecipientSummary>,
                          private val context: Context
) :
    RecyclerView.Adapter<TopRecipientAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipientName: TextView = view.findViewById(R.id.textRecipientName)
        val totalAmount: TextView = view.findViewById(R.id.textTotalAmount)
        val rank: TextView = view.findViewById(R.id.textRank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipient, parent, false)
        return ViewHolder(view)
    }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipient = recipients[position]
            holder.recipientName.text = recipient.recipient
            holder.totalAmount.text = CurrencyUtils.formatAmount(context, recipient.totalAmount)
            holder.rank.text = "${position + 1}."
        }

    override fun getItemCount() = recipients.size

    fun updateData(newRecipients: List<RecipientSummary>) {
        recipients = newRecipients
        notifyDataSetChanged()
    }
}