package com.example.e_commerce.ui.login.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.CountryItemLayoutBinding
import com.example.e_commerce.ui.login.models.CountryUiModel

class CountriesAdapter(
    private val countries: List<CountryUiModel>,
    private val onCountryClickListener: OnCountryClickListener
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(private val binding: CountryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: CountryUiModel) {
            binding.country = country
            binding.root.setOnClickListener {
                onCountryClickListener.onCountryClicked(country)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            CountryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

}

interface OnCountryClickListener {
    fun onCountryClicked(country: CountryUiModel)
}