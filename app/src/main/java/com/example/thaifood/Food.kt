package com.example.thaifood

import java.text.NumberFormat
import java.util.Locale

data class Food(
    var name: String,
    var price: Int, var imageResId: Int,
    var description: String? = null,
    var viewType: Int = TYPE_COLLECTION,
    var imageUri: String = ""
) {
    companion object {
        const val TYPE_COLLECTION = 1
        const val TYPE_FEATURED = 2
        const val TYPE_COMBO = 3
    }
}

fun Int.toVND(): String {
    val format = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    return "${format.format(this)}đ"
}