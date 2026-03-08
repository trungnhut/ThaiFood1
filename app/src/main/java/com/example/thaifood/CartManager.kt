package com.example.thaifood
object CartManager {
    val cartList = mutableListOf<CartItem>()
    fun addToCart(item: CartItem) {
        // Kiểm tra món này đã có trong giỏ chưa, có rồi thì cộng dồn số lượng
        val existingItem = cartList.find { it.name == item.name }
        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            cartList.add(item)
        }
    }
}