package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var tvCartTotal: TextView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        tvCartTotal = findViewById(R.id.tvCartTotal)
        val rvCartItems = findViewById<RecyclerView>(R.id.rvCartItems)
        val btnContinueShopping = findViewById<Button>(R.id.btnContinueShopping)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)
        val etCartNote = findViewById<EditText>(R.id.etCartNote)

        rvCartItems.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(CartManager.cartList) {

            calculateTotal()
        }
        rvCartItems.adapter = cartAdapter

        calculateTotal()

        btnContinueShopping.setOnClickListener {
            finish()
        }

        // Nút Đặt hàng
        btnCheckout.setOnClickListener {
            if (CartManager.cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống, hãy chọn món nhé!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var orderDetails = ""
            var totalPrice = 0
            for (item in CartManager.cartList) {
                orderDetails += "${item.quantity}x ${item.name}\n"
                totalPrice += (item.price * item.quantity)
            }

            val ghiChu = etCartNote.text.toString().trim()
            if (ghiChu.isNotEmpty()) {
                orderDetails += "Ghi chú: $ghiChu"
            }

            val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
            val customerName = sharedPref.getString("fullName", "Khách ẩn danh") ?: "Khách"

            val newOrder = Order(
                customerName = customerName,
                details = orderDetails,
                totalPrice = totalPrice,
                status = 0
            )

            val db = DatabaseHelper(this)
            val newOrderId = db.insertOrder(newOrder)

            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show()

            CartManager.cartList.clear()
            cartAdapter.notifyDataSetChanged()
            calculateTotal()
            etCartNote.setText("")

            val intent = Intent(this, OrderStatusActivity::class.java)
            intent.putExtra("ORDER_ID", newOrderId.toInt())
            startActivity(intent)
            finish()
        }
    }

    private fun calculateTotal() {
        var total = 0
        for (item in CartManager.cartList) {
            total += (item.price * item.quantity)
        }
        tvCartTotal.text = "Tổng cộng: ${total.toVND()}"
    }
}