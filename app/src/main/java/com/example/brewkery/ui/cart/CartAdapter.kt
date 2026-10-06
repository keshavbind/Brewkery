package com.example.brewkery.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brewkery.data.cart.CartItem
import com.example.brewkery.data.cart.CartManager
import com.example.brewkery.databinding.ItemCartBinding

class CartAdapter(
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var items = CartManager.getItems().toMutableList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {

        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    private fun refreshItems() {
        items = CartManager.getItems().toMutableList()
        notifyDataSetChanged()
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem, position: Int) {

            binding.tvCartProductName.text =
                item.product.name

            binding.tvCartCustomization.text =
                "${item.selectedSize} • " +
                        "${item.selectedMilk} • " +
                        "${item.selectedSugar}"

            binding.tvCartPrice.text =
                "$${String.format("%.2f", item.finalPrice * item.quantity)}"

            binding.tvCartQuantity.text =
                item.quantity.toString()

            binding.btnIncrease.setOnClickListener {

                CartManager.increaseQuantity(position)

                refreshItems()
                onCartChanged()
            }

            binding.btnDecrease.setOnClickListener {

                CartManager.decreaseQuantity(position)

                refreshItems()
                onCartChanged()
            }
        }
    }
}