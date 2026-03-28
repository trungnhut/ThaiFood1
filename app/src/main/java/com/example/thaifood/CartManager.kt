package com.example.thaifood
object CartManager {
    val cartList = mutableListOf<CartItem>()
    fun addToCart(item: CartItem) {
        val existingItem = cartList.find { it.name == item.name }
        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            cartList.add(item)
        }
    }
}