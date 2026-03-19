package com.example.thaifood

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private var orderList: List<Order>,
    private val onCancelClick: (Order) -> Unit // Sự kiện khi bấm nút Hủy
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCustomer: TextView = itemView.findViewById(R.id.tvOrderCustomer)
        val tvDetails: TextView = itemView.findViewById(R.id.tvOrderDetails)
        val tvPrice: TextView = itemView.findViewById(R.id.tvOrderPrice)
        val tvStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val btnCancel: Button = itemView.findViewById(R.id.btnCancelOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.tvCustomer.text = "Khách hàng: ${order.customerName}"
        holder.tvDetails.text = order.details
        holder.tvPrice.text = "Tổng: ${order.totalPrice}đ"

        // Xử lý trạng thái (0: Chờ xử lý, 1: Đã hủy)
        if (order.status == 0) {
            holder.tvStatus.text = "Chờ xử lý"
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Màu xanh lá
            holder.btnCancel.visibility = View.VISIBLE // Hiện nút hủy

            // Bấm nút hủy
            holder.btnCancel.setOnClickListener {
                onCancelClick(order)
            }
        } else {
            holder.tvStatus.text = "Đã hủy"
            holder.tvStatus.setTextColor(Color.parseColor("#F44336")) // Màu đỏ
            holder.btnCancel.visibility = View.GONE // Ẩn nút hủy đi vì đã hủy rồi
        }
    }

    override fun getItemCount(): Int = orderList.size

    // Hàm cập nhật lại danh sách khi có thay đổi
    fun updateData(newList: List<Order>) {
        orderList = newList
        notifyDataSetChanged()
    }
}