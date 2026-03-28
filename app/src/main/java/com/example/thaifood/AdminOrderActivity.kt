package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminOrderActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var rvOrders: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order)

        db = DatabaseHelper(this)
        rvOrders = findViewById(R.id.rvAdminOrders)
        val btnBack = findViewById<ImageView>(R.id.btnBackAdmin)

        btnBack.setOnClickListener {
            finish()
        }

        rvOrders.layoutManager = LinearLayoutManager(this)

        rvOrders.layoutManager = LinearLayoutManager(this)

        orderAdapter = OrderAdapter(emptyList(),
            onCancelClick = { orderToCancel ->
                db.updateOrderStatus(orderToCancel.id, 1)
                Toast.makeText(this, "Đã hủy đơn của ${orderToCancel.customerName}", Toast.LENGTH_SHORT).show()
                loadOrders()
            },
            onConfirmClick = { orderToConfirm ->
                db.updateOrderStatus(orderToConfirm.id, 2)
                Toast.makeText(this, "Đã xác nhận đơn của ${orderToConfirm.customerName}", Toast.LENGTH_SHORT).show()
                loadOrders()
            }
        )
        rvOrders.adapter = orderAdapter

        loadOrders()
    }
    override fun onResume() {
        super.onResume()
        loadOrders()
    }
    private fun loadOrders() {
        val orders = db.getAllOrders()
        orderAdapter.updateData(orders)
    }
}