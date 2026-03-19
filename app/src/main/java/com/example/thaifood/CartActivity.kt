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

            // 1. Gom chi tiết giỏ hàng thành 1 chuỗi văn bản
            var orderDetails = ""
            var totalPrice = 0
            for (item in CartManager.cartList) {
                orderDetails += "${item.quantity}x ${item.name}\n"
                totalPrice += (item.price * item.quantity)
            }

            // Ghi chú của khách
            val ghiChu = etCartNote.text.toString().trim()
            if (ghiChu.isNotEmpty()) {
                orderDetails += "Ghi chú: $ghiChu"
            }

            // 2. Lấy tên khách hàng từ SharedPreferences (đã lưu lúc đăng nhập)
            val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
            val customerName = sharedPref.getString("fullName", "Khách ẩn danh") ?: "Khách"

            // 3. Tạo đối tượng Order và lưu vào DB
            val newOrder = Order(
                customerName = customerName,
                details = orderDetails,
                totalPrice = totalPrice,
                status = 0 // 0: Đang chờ xử lý
            )

            val db = DatabaseHelper(this)
            db.insertOrder(newOrder)

            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show()

            // 4. Xóa giỏ hàng và cập nhật giao diện
            CartManager.cartList.clear()
            cartAdapter.notifyDataSetChanged()
            calculateTotal()
            etCartNote.setText("") // Xóa trắng ô ghi chú
            val intent = Intent(this, OrderStatusActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun calculateTotal() {
        var total = 0
        for (item in CartManager.cartList) {
            total += (item.price * item.quantity)
        }
        tvCartTotal.text = "Tổng cộng: ${total}đ"
    }
}