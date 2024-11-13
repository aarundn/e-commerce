package com.example.e_commerce.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ItemSalesAdBinding
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import com.example.e_commerce.utils.CountdownTimer
import kotlinx.coroutines.cancel

class SalesAdAdapter(
     private val salesAds: List<SalesUiAdModel>
) : RecyclerView.Adapter<SalesAdAdapter.SalesAdViewHolder>() {

    val timerList = mutableMapOf<String, CountdownTimer>()
    inner class SalesAdViewHolder(private val binding: ItemSalesAdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(salesAd: SalesUiAdModel) {
            binding.salesAd = salesAd
            salesAd.endAt?.let {
                timerList[salesAd.id ?: ""]?.cancel()
                timerList.remove(salesAd.id ?: "")
                val timer = CountdownTimer(it) { hours, minutes, seconds ->
                    binding.hoursTextView.text = hours.toString()
                    binding.minutesTextView.text = minutes.toString()
                    binding.secondsTextView.text = seconds.toString()
                }
                timer.start()
                timerList.put(salesAd.id ?: "", timer)
            }

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

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        timerList.forEach { (_, timer) ->
            timer.cancel()
        }
    }
}