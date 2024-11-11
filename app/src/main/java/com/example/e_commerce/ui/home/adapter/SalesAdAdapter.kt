package com.example.e_commerce.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ItemSalesAdBinding
import com.example.e_commerce.ui.home.model.SalesUiAdModel

class SalesAdAdapter(
     private val salesAds: List<SalesUiAdModel>
) : RecyclerView.Adapter<SalesAdAdapter.SalesAdViewHolder>() {

    inner class SalesAdViewHolder(private val binding: ItemSalesAdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(salesAd: SalesUiAdModel) {
            binding.salesAd = salesAd
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesAdViewHolder {
        val binding = ItemSalesAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SalesAdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SalesAdViewHolder, position: Int) {
        holder.bind(salesAds[position])
    }

    override fun getItemCount(): Int = salesAds.size
}