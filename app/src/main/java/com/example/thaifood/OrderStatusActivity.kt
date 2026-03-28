package com.example.thaifood

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OrderStatusActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private var orderId: Int = -1
    private lateinit var btnCancel: Button
    private lateinit var tvStatusTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        db = DatabaseHelper(this)
        orderId = intent.getIntExtra("ORDER_ID", -1)

        val btnBackToHome = findViewById<Button>(R.id.btnBackToHomeStatus)
        btnCancel = findViewById(R.id.btnCancelOrderCustomer)
        tvStatusTitle = findViewById(R.id.tvStatusTitle)
        btnBackToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnCancel.setOnClickListener {
            if (orderId != -1) {
                db.updateOrderStatus(orderId, 1)
                Toast.makeText(this, "Đã hủy đơn hàng!", Toast.LENGTH_SHORT).show()

                updateUI()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        if (orderId == -1) return
        val order = db.getOrderById(orderId) ?: return

        when (order.status) {
            0 -> {
                tvStatusTitle.text = "Đơn Hàng Chờ Xác Nhận"
                tvStatusTitle.setTextColor(Color.parseColor("#FF9800"))
                btnCancel.visibility = View.VISIBLE
            }
            1 -> {
                tvStatusTitle.text = "Đơn Hàng Đã Hủy"
                tvStatusTitle.setTextColor(Color.parseColor("#F44336"))
                btnCancel.visibility = View.GONE
            }
            2 -> {
                tvStatusTitle.text = "Nhà Hàng Đang Chuẩn Bị"
                tvStatusTitle.setTextColor(Color.parseColor("#4CAF50"))
                btnCancel.visibility = View.GONE
            }
        }
    }
}