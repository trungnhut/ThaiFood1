package com.example.thaifood

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
            } else {
                val ghiChu = etCartNote.text.toString()
                Toast.makeText(this, "Đặt hàng thành công! Ghi chú: $ghiChu", Toast.LENGTH_LONG).show()


                CartManager.cartList.clear()
                cartAdapter.notifyDataSetChanged()
                calculateTotal()
            }
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