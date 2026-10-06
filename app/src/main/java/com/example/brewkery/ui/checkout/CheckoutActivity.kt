package com.example.brewkery.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brewkery.data.cart.CartManager
import com.example.brewkery.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupCheckout()
    }

    private fun setupCheckout() {

        val items = CartManager.getItems()

        if (items.isEmpty()) {
            finish()
            return
        }

        val subtotal = CartManager.getTotal()
        val deliveryFee = 40.0
        val tax = subtotal * 0.05
        val total = subtotal + deliveryFee + tax

        val itemSummary = items.joinToString("\n") {
            "${it.product.name} × ${it.quantity} — ₹${String.format("%.2f", it.finalPrice * it.quantity)}"
        }

        binding.tvCheckoutItems.text = itemSummary

        binding.tvCheckoutSubtotal.text =
            "Subtotal: ₹${String.format("%.2f", subtotal)}"

        binding.tvCheckoutDelivery.text =
            "Delivery: ₹${String.format("%.2f", deliveryFee)}"

        binding.tvCheckoutTax.text =
            "Tax: ₹${String.format("%.2f", tax)}"

        binding.tvCheckoutTotal.text =
            "Total: ₹${String.format("%.2f", total)}"

        binding.rbCash.isChecked = true

        binding.btnPlaceOrder.setOnClickListener {

            val address = binding.etAddress.text.toString().trim()

            if (address.isEmpty()) {
                binding.etAddress.error =
                    "Please enter your delivery address"
                return@setOnClickListener
            }

            val paymentMethod =
                if (binding.rbCash.isChecked) {
                    "Cash on Delivery"
                } else {
                    "Online Payment"
                }

            // Get number of items before clearing the cart
            val itemCount = CartManager.getItems().sumOf {
                it.quantity
            }

            // Generate ticket ID
            val ticketId =
                "BK-" + (10000..99999).random()

            // Clear cart after placing order
            CartManager.clearCart()

            // Open Order Status screen
            val intent = Intent(
                this,
                com.example.brewkery.ui.order.OrderStatusActivity::class.java
            )

            intent.putExtra("TICKET_ID", ticketId)
            intent.putExtra("ITEM_COUNT", itemCount)

            startActivity(intent)

            finish()
        }
    }
}