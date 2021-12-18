package com.example.localbuddy.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.entity.Product
import com.example.localbuddy.databinding.ListItemProductBinding
import com.example.localbuddy.imgUrl

class ProductsAdapter(val onProductClicked: (productId: String) -> Unit): ListAdapter<Product,ProductsAdapter.ProductViewHolder>(DiffCallback) {
    inner class ProductViewHolder(private var binding: ListItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product){
            binding.productTitle.text = product.name
            binding.productDesc.text = product.description
            binding.productImage.imgUrl(product.photos[0].url)
            binding.productPrice.text = "Rs. ${product.price}"
            binding.root.setOnClickListener {
                onProductClicked(product.id)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        return ProductViewHolder(ListItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
}