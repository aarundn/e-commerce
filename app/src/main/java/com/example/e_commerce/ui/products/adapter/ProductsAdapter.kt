package com.example.e_commerce.ui.products.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ProductItemBinding
import com.example.e_commerce.ui.products.models.ProductUIModel
enum class ProductViewType{
    GRID,
    LIST
}
class ProductAdapter(
    private val viewType: ProductViewType = ProductViewType.GRID,
    private val onProductClick: (ProductUIModel) -> Unit = {}
) : ListAdapter<ProductUIModel, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        Log.d("ProductAdapter", "onBindViewHolder: $product")
        holder.bind(viewType,product)
    }

    inner class ProductViewHolder(
        private val binding: ProductItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewType: ProductViewType,product: ProductUIModel) {
            if (viewType == ProductViewType.GRID){
                binding.productItem.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            binding.product = product
//            binding.root.setOnClickListener { onProductClicked(product) }
            binding.executePendingBindings()
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductUIModel>() {
    override fun areItemsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
        // Replace 'id' with a unique identifier for the product
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
        return oldItem == newItem
    }
}