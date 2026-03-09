package com.example.thaifood

data class Food(
    var name: String,
    var price: Int, var imageResId: Int,
    var description: String? = null,
    var viewType: Int = TYPE_COLLECTION

) {
    companion object {
        const val TYPE_COLLECTION = 1
        const val TYPE_FEATURED = 2
        const val TYPE_COMBO = 3
    }
}