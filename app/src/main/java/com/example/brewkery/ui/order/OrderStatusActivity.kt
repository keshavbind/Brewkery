package com.example.brewkery.ui.order

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brewkery.MainActivity
import com.example.brewkery.databinding.ActivityOrderStatusBinding

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ticketId =
            intent.getStringExtra("TICKET_ID") ?: "BK-00000"

        val itemCount =
            intent.getIntExtra("ITEM_COUNT", 0)

        binding.tvTicketId.text =
            "Order Ticket #$ticketId"

        binding.tvItemsOrdered.text =
            "Items Ordered: $itemCount Items"

        binding.btnBackToMenu.setOnClickListener {

            val intent = Intent(
                this,
                MainActivity::class.java
            )

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)

            finish()
        }
    }
}
