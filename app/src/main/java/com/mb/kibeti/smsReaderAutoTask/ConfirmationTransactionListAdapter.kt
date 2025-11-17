package com.mb.kibeti.smsReaderAutoTask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.databinding.ItemAutoAllocationConfirmTransactionBinding
import com.mb.kibeti.smsReaderAutoTask.responses.Data

class ConfirmationTransactionListAdapter(
    private val transactions: List<Data>,
) : RecyclerView.Adapter<ConfirmationTransactionListAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: ItemAutoAllocationConfirmTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Data) {
            binding.tvRecipientName.text = transaction.trans_name
            binding.tvAmount .text = transaction.amount
            binding.tvCategory.text = transaction.allocation


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemAutoAllocationConfirmTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size
}
