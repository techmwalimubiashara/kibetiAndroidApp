package com.mb.kibeti.smsReaderAutoTask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.databinding.ItemExpenditureFileBinding

class ExpenditureAdapter(private val expenditureList : List<ExpenditureModel>) :
    RecyclerView.Adapter<ExpenditureAdapter.ExpenditureViewHolder>() {


    inner class ExpenditureViewHolder(private val bind : ItemExpenditureFileBinding):RecyclerView.ViewHolder(bind.root) {
       fun bind (expenditure : ExpenditureModel){
           bind.recipientText.text = expenditure.recipient
           bind.categoryText.text = expenditure.category
           bind.amountText.text = expenditure.amount.toString()
           bind.dateText.text = expenditure.date

       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenditureViewHolder {
        val bind = ItemExpenditureFileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpenditureViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return expenditureList.size
    }

    override fun onBindViewHolder(holder: ExpenditureViewHolder, position: Int) {
        holder.bind(expenditureList[position])
    }
}