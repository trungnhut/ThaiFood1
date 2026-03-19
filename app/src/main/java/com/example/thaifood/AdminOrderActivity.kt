package com.example.thaifood

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

        // Nút quay lại
        btnBack.setOnClickListener {
            finish()
        }

        // Cài đặt RecyclerView
        rvOrders.layoutManager = LinearLayoutManager(this)

        // Khởi tạo Adapter với danh sách rỗng, kèm theo hành động Hủy đơn
        orderAdapter = OrderAdapter(emptyList()) { orderToCancel ->
            // Khi Admin bấm nút Hủy ở 1 đơn hàng:
            db.updateOrderStatus(orderToCancel.id, 1) // 1 là trạng thái Đã hủy
            Toast.makeText(this, "Đã hủy đơn của ${orderToCancel.customerName}", Toast.LENGTH_SHORT).show()

            // Tải lại danh sách sau khi hủy
            loadOrders()
        }
        rvOrders.adapter = orderAdapter

        // Lần đầu mở màn hình -> tải dữ liệu
        loadOrders()
    }

    private fun loadOrders() {
        // Lấy tất cả đơn hàng từ SQLite
        val orders = db.getAllOrders()
        // Cập nhật vào Adapter
        orderAdapter.updateData(orders)
    }
}