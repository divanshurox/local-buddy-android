package com.example.localbuddy.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.entity.CartItem
import com.example.localbuddy.databinding.ListItemCartBinding
import com.example.localbuddy.imgUrl

class CartListAdapter(
    val removeCartItem: (productId: String) -> Unit,
    val inc: (productId: String) -> Unit,
    val dec: (productId: String) -> Unit
) : ListAdapter<CartItem, CartListAdapter.CartViewHolder>(DiffCallback) {

    inner class CartViewHolder(private val binding: ListItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.apply {
                productImage.imgUrl(cartItem.product.photos[0].url)
                productName.text = cartItem.product.name
                productPrice.text = cartItem.product.price.toString()
                deleteItem.setOnClickListener {
                    removeCartItem(cartItem.product.id)
                    notifyItemRemoved(adapterPosition)
                }
                productQuantity.text = cartItem.quantity.toString()
                inc.setOnClickListener {
                    inc(cartItem.product.id)
                    notifyItemChanged(adapterPosition)
                }
                dec.setOnClickListener {
                    dec(cartItem.product.id)
                    notifyItemChanged(adapterPosition)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ListItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
}