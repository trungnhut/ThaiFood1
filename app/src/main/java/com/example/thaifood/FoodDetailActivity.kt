package com.example.thaifood

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FoodDetailActivity : AppCompatActivity() {

    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        val name = intent.getStringExtra("FOOD_NAME") ?: ""
        val price = intent.getIntExtra("FOOD_PRICE", 0)
        val imageRes = intent.getIntExtra("FOOD_IMAGE", 0)
        val description = intent.getStringExtra("FOOD_DESC") ?: ""

        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvPrice = findViewById<TextView>(R.id.tvDetailPrice)
        val imgFood = findViewById<ImageView>(R.id.imgDetailFood)
        val tvQuantity = findViewById<TextView>(R.id.tvQuantity)
        val tvDesc = findViewById<TextView>(R.id.tvDetailDescription)

        tvName.text = name
        tvPrice.text = "Tổng: ${price}đ"
        if (imageRes != 0) imgFood.setImageResource(imageRes)

        if (description.isNotEmpty()) {
            tvDesc.text = description
            tvDesc.visibility = View.VISIBLE
        } else {
            tvDesc.visibility = View.GONE
        }

        findViewById<Button>(R.id.btnMinus).setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
                tvPrice.text = "Tổng: ${price * quantity}đ"
            }
        }

        findViewById<Button>(R.id.btnPlus).setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
            tvPrice.text = "Tổng: ${price * quantity}đ"
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnAddCart).setOnClickListener {
            val item = CartItem(name, price, quantity, imageRes)
            CartManager.addToCart(item)
            Toast.makeText(this, "Đã thêm $quantity $name vào giỏ", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}