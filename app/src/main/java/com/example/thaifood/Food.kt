package com.example.thaifood

data class Food(
    val name: String,
    val price: Int,
    val imageResId: Int,
    val promoText: String = "",
    val viewType: Int,
    val description: String = ""
) {
    companion object {
        const val TYPE_COLLECTION = 1
        const val TYPE_FEATURED = 2
        const val TYPE_COMBO = 3
    }
}