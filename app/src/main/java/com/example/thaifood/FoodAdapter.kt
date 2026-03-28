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

    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgCollection)
        val tvName: TextView = itemView.findViewById(R.id.tvCollectionName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCollectionPrice)
    }

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
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            Food.TYPE_COLLECTION -> {
                val view = inflater.inflate(R.layout.item_collection, parent, false)
                CollectionViewHolder(view)
            }
            Food.TYPE_COMBO -> {
                val view = inflater.inflate(R.layout.item_combo, parent, false)
                ComboViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_food, parent, false)
                FeaturedViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        fun loadImage(imgView: ImageView) {
            if (item.imageUri.isNotEmpty()) {
                imgView.setImageURI(android.net.Uri.parse(item.imageUri))
            } else {
                imgView.setImageResource(item.imageResId)
            }
        }

        when (holder) {
            is CollectionViewHolder -> {
                loadImage(holder.img)
                holder.tvName.text = item.name
                holder.tvPrice.text = item.price.toVND()
            }
            is FeaturedViewHolder -> {
                loadImage(holder.img)
                holder.tvName.text = item.name
                holder.tvPrice.text = item.price.toVND()
            }
            is ComboViewHolder -> {
                loadImage(holder.img)
                holder.tvName.text = item.name
                holder.tvPrice.text = item.price.toVND()
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = itemList.size
}