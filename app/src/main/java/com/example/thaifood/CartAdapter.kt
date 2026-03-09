package com.example.thaifood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val cartList: MutableList<CartItem>,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFood: ImageView = itemView.findViewById(R.id.imgCartItem)
        val tvName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvCartItemQuantity)
        val btnMinus: TextView = itemView.findViewById(R.id.btnMinus)
        val btnPlus: TextView = itemView.findViewById(R.id.btnPlus)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]

        holder.imgFood.setImageResource(item.imageRes)
        holder.tvName.text = item.name
        holder.tvPrice.text = "${item.price * item.quantity}đ"
        holder.tvQuantity.text = item.quantity.toString()

        holder.btnPlus.setOnClickListener {
            item.quantity++
            notifyItemChanged(position)
            onCartUpdated()
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                notifyItemChanged(position)
                onCartUpdated()
            }
        }

        holder.btnDelete.setOnClickListener {
            cartList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
            onCartUpdated()
        }
    }

    override fun getItemCount(): Int = cartList.size
}