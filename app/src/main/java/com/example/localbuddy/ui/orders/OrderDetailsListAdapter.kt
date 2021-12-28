package com.example.localbuddy.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.entity.CartItem
import com.example.localbuddy.databinding.ListItemOrderDetailBinding
import com.example.localbuddy.imgUrl

class OrderDetailsListAdapter: ListAdapter<CartItem, OrderDetailsListAdapter.OrderDetailsViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }

    inner class OrderDetailsViewHolder(private var binding: ListItemOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.apply {
                productImage.imgUrl(item.product.photos[0].url)
                productName.text = item.product.name
                productPrice.text = "Rs. ${item.product.price}"
                productQuantity.text = "Qty: ${item.quantity}"
            }
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsListAdapter.OrderDetailsViewHolder {
        return OrderDetailsViewHolder(
            ListItemOrderDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}