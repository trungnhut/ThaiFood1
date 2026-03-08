package com.example.thaifood

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartContainer = findViewById<LinearLayout>(R.id.cartContainer)

        // Duyệt qua danh sách các món trong CartManager
        for (item in CartManager.cartList) {
            // Tạo 1 TextView đơn giản để hiển thị (Bạn có thể tự inflate cái CardView XML của bạn nếu muốn đẹp hơn)
            val textView = TextView(this)
            textView.text = "${item.name} - Số lượng: ${item.quantity} - Tổng: ${item.price * item.quantity}đ"
            textView.textSize = 16f
            textView.setPadding(16, 16, 16, 16)

            cartContainer.addView(textView)
        }

        if (CartManager.cartList.isEmpty()) {
            val emptyTxt = TextView(this)
            emptyTxt.text = "Giỏ hàng đang trống!"
            emptyTxt.setPadding(16, 16, 16, 16)
            cartContainer.addView(emptyTxt)
        }
    }
}