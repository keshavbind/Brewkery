package com.example.brewkery.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.brewkery.R
import com.example.brewkery.data.model.Product
import com.example.brewkery.databinding.ItemProductBinding

class ProductAdapter(
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()

    fun submitList(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductViewHolder(binding, onProductClick)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val onProductClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            binding.tvProductName.text = product.name

            binding.tvTagline.text = product.tagline

            binding.tvRating.text =
                "★ ${product.rating} (${product.reviewCount} reviews)"

            binding.tvPrice.text =
                "₹${String.format("%.2f", product.basePrice)}"

            Glide.with(binding.ivProduct.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.ivProduct)

            binding.root.setOnClickListener {
                onProductClick(product)
            }
        }
    }
}