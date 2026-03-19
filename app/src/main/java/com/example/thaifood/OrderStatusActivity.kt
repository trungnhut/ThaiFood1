package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OrderStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        val btnBackToHome = findViewById<Button>(R.id.btnBackToHomeStatus)
        val btnCancel = findViewById<Button>(R.id.btnCancelOrderCustomer) // Tìm nút Hủy

        // Xử lý khi bấm nút Về trang chủ
        btnBackToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Xử lý khi bấm nút Hủy đơn
        btnCancel.setOnClickListener {
            // Tạm thời hiển thị thông báo và quay về trang chủ
            Toast.makeText(this, "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()

            /* Lưu ý nhỏ: Ở phiên bản nâng cao, chỗ này bạn sẽ cần gọi DatabaseHelper
             để đổi trạng thái (status = 1) của đơn hàng này trong SQLite.
             */
        }
    }
}