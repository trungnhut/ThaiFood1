package com.example.thaifood

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AddFoodActivity : AppCompatActivity() {

    private lateinit var imgFood: ImageView
    private lateinit var btnSave: Button

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        imgFood = findViewById(R.id.imgFood)
        btnSave = findViewById(R.id.btnSave)

        val btnBack = findViewById<Button>(R.id.btnBack)

        db = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.etFoodName)
        val etPrice = findViewById<EditText>(R.id.etFoodPrice)
        val etDesc = findViewById<EditText>(R.id.etFoodDesc)

        // hiển thị ảnh mặc định
        imgFood.setImageResource(R.drawable.padthai)

        // lưu món ăn
        btnSave.setOnClickListener {

            val food = Food(
                name = etName.text.toString(),
                price = etPrice.text.toString().toInt(),
                description = etDesc.text.toString(),
                imageResId = R.drawable.padthai,
                viewType = Food.TYPE_COLLECTION
            )

            db.insertFood(food)

            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}