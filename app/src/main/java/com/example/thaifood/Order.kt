package com.example.thaifood

data class Order(
    var id: Int = 0,
    var customerName: String,
    var details: String,
    var totalPrice: Int,
    var status: Int
)