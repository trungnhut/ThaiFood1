package com.example.thaifood

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        tvPrice.text = "Tổng: ${(price * quantity).toVND()}"
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
                tvPrice.text = "Tổng: ${(price * quantity).toVND()}"
            }
        }

        findViewById<Button>(R.id.btnPlus).setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
            tvPrice.text = "Tổng: ${(price * quantity).toVND()}"
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


        val btnDeleteFood = findViewById<Button?>(R.id.btnDeleteFood)
        val btnEditFood = findViewById<Button?>(R.id.btnEditFood)

        val sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val role = sharedPref.getInt("role", 0)

        if (role == 1) {
            btnDeleteFood?.visibility = View.VISIBLE
            btnEditFood?.visibility = View.VISIBLE
        }


        btnDeleteFood?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa món")
                .setMessage("Bạn có chắc chắn muốn xóa món '$name' không? Hành động này không thể hoàn tác.")
                .setPositiveButton("Xóa") { _, _ ->
                    val db = DatabaseHelper(this)
                    if (name.isNotEmpty()) {
                        db.deleteFoodByName(name)
                        Toast.makeText(this, "Đã xóa món $name thành công!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("Hủy", null)
                .show()
        }

        btnEditFood?.setOnClickListener {
            val intentEdit = Intent(this, AddFoodActivity::class.java)
            intentEdit.putExtra("EDIT_FOOD_NAME", name)
            intentEdit.putExtra("EDIT_FOOD_PRICE", price)
            intentEdit.putExtra("EDIT_FOOD_DESC", description)
            startActivity(intentEdit)
            finish()
        }
    }
}