package com.example.e_commerce.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ItemProductImagesBinding
import com.example.e_commerce.databinding.ItemSalesAdBinding
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import com.example.e_commerce.utils.CountdownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductImagesAdAdapter(
     private val  productImages: List<String>
) : RecyclerView.Adapter<ProductImagesAdAdapter.ProductImageAdViewHolder>() {

    inner class ProductImageAdViewHolder(private val binding: ItemProductImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {

            binding.imageUrl = imageUrl
            binding.executePendingBindings()

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageAdViewHolder {
        val binding = ItemProductImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductImageAdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImageAdViewHolder, position: Int) {
        holder.bind(productImages[position])
    }

    override fun getItemCount(): Int = productImages.size

}

