package com.example.brewkery.data.cart

object CartManager {

    private val items = mutableListOf<CartItem>()

    fun addItem(newItem: CartItem) {

        // Check if the exact same product and customization
        // already exists in the cart
        val existingIndex = items.indexOfFirst {
            it.product.id == newItem.product.id &&
                    it.selectedSize == newItem.selectedSize &&
                    it.selectedMilk == newItem.selectedMilk &&
                    it.selectedSugar == newItem.selectedSugar
        }

        if (existingIndex != -1) {

            val existingItem = items[existingIndex]

            items[existingIndex] = existingItem.copy(
                quantity = existingItem.quantity + newItem.quantity
            )

        } else {

            items.add(newItem)
        }
    }

    fun increaseQuantity(position: Int) {

        if (position in items.indices) {

            val item = items[position]

            items[position] = item.copy(
                quantity = item.quantity + 1
            )
        }
    }

    fun decreaseQuantity(position: Int) {

        if (position in items.indices) {

            val item = items[position]

            if (item.quantity > 1) {

                items[position] = item.copy(
                    quantity = item.quantity - 1
                )

            } else {

                items.removeAt(position)
            }
        }
    }

    fun getItems(): List<CartItem> {
        return items.toList()
    }

    fun getTotal(): Double {
        return items.sumOf {
            it.finalPrice * it.quantity
        }
    }

    fun clearCart() {
        items.clear()
    }
}