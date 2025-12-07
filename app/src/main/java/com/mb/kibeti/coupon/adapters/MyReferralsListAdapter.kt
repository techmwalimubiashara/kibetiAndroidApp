package com.mb.kibeti.coupon.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.coupon.responses.MyReferralsApiResponseItem
import com.mb.kibeti.databinding.ItemReferralFileBinding

class MyReferralsListAdapter(private val referrals : List<MyReferralsApiResponseItem>):
RecyclerView.Adapter<MyReferralsListAdapter.MyReferralViewHolder>() {

    inner class MyReferralViewHolder(val binding: ItemReferralFileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReferralViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReferralFileBinding.inflate(inflater, parent, false)
        return MyReferralViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyReferralViewHolder, position: Int) {
        val referral = referrals[position]
        holder.binding.referralName.text = referral.name
          holder.binding.referralLevel.text = referral.referral_level.toString()
        holder.binding.referralDate.text = referral.created_at
        Log.d("Adapter", "Binding: ${referral.name}")

    }

    override fun getItemCount(): Int = referrals.size
}
