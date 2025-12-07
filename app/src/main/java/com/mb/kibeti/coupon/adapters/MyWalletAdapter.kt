package com.mb.kibeti.coupon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.coupon.responses.Transaction
import com.mb.kibeti.databinding.ItemTransactionFileBinding
import java.text.NumberFormat
import java.util.Locale

class MyWalletAdapter (private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<MyWalletAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: ItemTransactionFileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        val context = holder.itemView.context

        holder.binding.apply {
            textViewLevel.text = transaction.level.toString()
            textViewAmount.text = transaction.amount.toFloat().toInt().toString()

        }
    }

    override fun getItemCount(): Int = transactionList.size

}