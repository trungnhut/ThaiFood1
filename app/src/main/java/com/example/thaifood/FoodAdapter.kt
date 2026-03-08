package com.example.thaifood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(

    private val itemList: List<Food>,
    private val onItemClick: (Food) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//Bộ sưu tầm
    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgCollection)
        val tvPromo: TextView = itemView.findViewById(R.id.tvPromoText)
    }

//Món Nổi Bật
    class FeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgFood)
        val tvName: TextView = itemView.findViewById(R.id.tvFoodName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvFoodPrice)
    }
    class ComboViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgCombo)
        val tvName: TextView = itemView.findViewById(R.id.tvComboName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvComboPrice)
    }


    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Food.TYPE_COLLECTION) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
            CollectionViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
            FeaturedViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        if (holder is CollectionViewHolder) {
            holder.img.setImageResource(item.imageResId)
            holder.tvPromo.text = item.promoText
        } else if (holder is FeaturedViewHolder) {
            holder.img.setImageResource(item.imageResId)
            holder.tvName.text = item.name
            holder.tvPrice.text = "${item.price}đ"
        }


        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = itemList.size
}