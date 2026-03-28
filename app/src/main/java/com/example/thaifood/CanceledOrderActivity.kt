package com.example.thaifood

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CanceledOrderActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var rvOrders: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canceled_order)

        db = DatabaseHelper(this)
        rvOrders = findViewById(R.id.rvCanceledOrders)
        val btnBack = findViewById<ImageView>(R.id.btnBackCanceled)

        btnBack.setOnClickListener {
            finish()
        }

        rvOrders.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val role = sharedPref.getInt("role", 0)
        val customerName = sharedPref.getString("fullName", "") ?: ""

        val canceledOrders = db.getCanceledOrders(role, customerName)

        orderAdapter = OrderAdapter(canceledOrders, {}, {})
        rvOrders.adapter = orderAdapter
    }
}