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
    private val onCancelClick: (Order) -> Unit,
    private val onConfirmClick: (Order) -> Unit // THÊM SỰ KIỆN XÁC NHẬN
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCustomer: TextView = itemView.findViewById(R.id.tvOrderCustomer)
        val tvDetails: TextView = itemView.findViewById(R.id.tvOrderDetails)
        val tvPrice: TextView = itemView.findViewById(R.id.tvOrderPrice)
        val tvStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val btnCancel: Button = itemView.findViewById(R.id.btnCancelOrder)
        val btnConfirm: Button = itemView.findViewById(R.id.btnConfirmOrder) // THÊM NÚT NÀY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.tvCustomer.text = "Khách hàng: ${order.customerName}"
        holder.tvDetails.text = order.details
        holder.tvPrice.text = "Tổng: ${order.totalPrice.toVND()}"

        // --- XỬ LÝ TRẠNG THÁI ---
        when (order.status) {
            0 -> { // Chờ xử lý
                holder.tvStatus.text = "Chờ xử lý"
                holder.tvStatus.setTextColor(Color.parseColor("#FF9800")) // Màu cam
                holder.btnCancel.visibility = View.VISIBLE
                holder.btnConfirm.visibility = View.VISIBLE
            }
            1 -> { // Đã hủy
                holder.tvStatus.text = "Đã hủy"
                holder.tvStatus.setTextColor(Color.parseColor("#F44336")) // Màu đỏ
                holder.btnCancel.visibility = View.GONE
                holder.btnConfirm.visibility = View.GONE
            }
            2 -> { // Đã xác nhận
                holder.tvStatus.text = "Đang chuẩn bị món"
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Màu xanh
                holder.btnCancel.visibility = View.GONE
                holder.btnConfirm.visibility = View.GONE
            }
        }

        // Bắt sự kiện click
        holder.btnCancel.setOnClickListener { onCancelClick(order) }
        holder.btnConfirm.setOnClickListener { onConfirmClick(order) }
    }

    override fun getItemCount(): Int = orderList.size

    fun updateData(newList: List<Order>) {
        orderList = newList
        notifyDataSetChanged()
    }
}