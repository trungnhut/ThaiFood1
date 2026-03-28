package com.example.thaifood

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class AddFoodActivity : AppCompatActivity() {
    private var selectedImageUri: Uri? = null

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

        val spCategory = findViewById<Spinner>(R.id.spFoodCategory)
        val categoryList = arrayOf("Món Chính", "Món Nổi Bật", "Combo Dành Cho Nhóm")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList)
        spCategory.adapter = spinnerAdapter

        imgFood.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        val originalName = intent.getStringExtra("EDIT_FOOD_NAME")

        if (originalName != null) {

            etName.setText(originalName)
            val oldPrice = intent.getIntExtra("EDIT_FOOD_PRICE", 0)
            if (oldPrice > 0) {
                etPrice.setText(oldPrice.toString())
            }
            etDesc.setText(intent.getStringExtra("EDIT_FOOD_DESC"))

            btnSave.text = "Cập nhật thông tin"
        }

        btnSave.setOnClickListener {
            val uriString = selectedImageUri?.toString() ?: ""

            val selectedType = when (spCategory.selectedItemPosition) {
                0 -> Food.TYPE_COLLECTION
                1 -> Food.TYPE_FEATURED
                2 -> Food.TYPE_COMBO
                else -> Food.TYPE_COLLECTION
            }

            val priceString = etPrice.text.toString().trim()
            val priceValue = if (priceString.isNotEmpty()) priceString.toInt() else 0

            val food = Food(
                name = etName.text.toString().trim(),
                price = priceValue,
                description = etDesc.text.toString().trim(),
                imageResId = R.drawable.padthai,
                viewType = selectedType,
                imageUri = uriString
            )

            if (originalName != null) {
                db.updateFood(food, originalName)
            } else {
                db.insertFood(food)
            }

            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedImageUri = uri
            imgFood.setImageURI(uri)
        }
    }
}