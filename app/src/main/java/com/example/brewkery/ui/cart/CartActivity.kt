package com.example.brewkery.ui.cart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brewkery.data.cart.CartManager
import com.example.brewkery.databinding.ActivityCartBinding
import com.example.brewkery.ui.checkout.CheckoutActivity

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back arrow
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupCart()
    }

    private fun setupCart() {

        binding.recyclerViewCart.layoutManager =
            LinearLayoutManager(this)

        cartAdapter = CartAdapter {
            updateTotals()
        }

        binding.recyclerViewCart.adapter = cartAdapter

        updateTotals()

        binding.btnCheckout.setOnClickListener {

            if (CartManager.getItems().isEmpty()) {
                return@setOnClickListener
            }

            startActivity(
                Intent(
                    this,
                    CheckoutActivity::class.java
                )
            )
        }
    }

    private fun updateTotals() {

        val subtotal = CartManager.getTotal()

        // Assignment requirement
        val deliveryFee = 2.50

        // 8% tax
        val tax = subtotal * 0.08

        val total = subtotal + deliveryFee + tax

        binding.tvSubtotal.text =
            "Subtotal: $${String.format("%.2f", subtotal)}"

        binding.tvDelivery.text =
            "Delivery: $${String.format("%.2f", deliveryFee)}"

        binding.tvTax.text =
            "Tax (8%): $${String.format("%.2f", tax)}"

        binding.tvTotal.text =
            "Total: $${String.format("%.2f", total)}"
    }
}