package com.example.thaifood

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvName = findViewById<TextView>(R.id.tvProfileName)
        val tvRole = findViewById<TextView>(R.id.tvProfileRole)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnManageOrders = findViewById<Button>(R.id.btnManageOrders) // Nút quản lý mới thêm

        val sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)

        // 1. Lấy thông tin hiển thị
        tvName.text = sharedPref.getString("fullName", "Người dùng")
        val role = sharedPref.getInt("role", 0)

        // 2. Phân quyền hiển thị
        if (role == 1) {
            tvRole.text = "Tài khoản: Admin"
            btnManageOrders.visibility = View.VISIBLE // Hiện nút nếu là Admin
        } else {
            tvRole.text = "Tài khoản: Khách hàng"
            btnManageOrders.visibility = View.GONE // Ẩn nút đi nếu là Khách
        }

        // 3. Xử lý khi bấm nút Quản lý đơn hàng
        btnManageOrders.setOnClickListener {
            startActivity(Intent(this, AdminOrderActivity::class.java))
        }

        // 4. Xử lý Đăng xuất
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