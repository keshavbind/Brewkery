package com.example.brewkery.data.cart

import com.example.brewkery.data.model.Product

data class CartItem(
    val product: Product,
    val selectedSize: String,
    val selectedMilk: String,
    val selectedSugar: String,
    val quantity: Int = 1,
    val finalPrice: Double
)
