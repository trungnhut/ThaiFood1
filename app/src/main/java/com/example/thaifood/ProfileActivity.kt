package com.example.thaifood

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvName = findViewById<TextView>(R.id.tvProfileName)
        val tvRole = findViewById<TextView>(R.id.tvProfileRole)
        val tvEmail = findViewById<TextView>(R.id.tvProfileEmail)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnManageOrders = findViewById<Button>(R.id.btnManageOrders)
        val btnOrderHistory = findViewById<Button>(R.id.btnOrderHistory)

        val sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)

        // 1. Lấy thông tin hiển thị
        tvName.text = sharedPref.getString("fullName", "Người dùng")

        val savedEmail = sharedPref.getString("email", "")
        if (savedEmail!!.isNotEmpty()) {
            tvEmail.text = savedEmail
        }

        val role = sharedPref.getInt("role", 0)

        if (role == 1) {
            tvRole.text = "Tài khoản: Admin"
            btnManageOrders.visibility = View.VISIBLE
            btnOrderHistory.visibility = View.GONE
        } else {
            tvRole.text = "Tài khoản: Khách hàng"
            btnManageOrders.visibility = View.GONE
            btnOrderHistory.visibility = View.VISIBLE
        }

        btnManageOrders.setOnClickListener {
            startActivity(Intent(this, AdminOrderActivity::class.java))
        }

        btnOrderHistory.setOnClickListener {
            Toast.makeText(this, "Sắp ra mắt chức năng xem lịch sử", Toast.LENGTH_SHORT).show()

        }

        btnLogout.setOnClickListener {
            val editor = sharedPref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}