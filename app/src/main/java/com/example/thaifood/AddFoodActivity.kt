package com.example.thaifood

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

        // 1. Cài đặt Spinner (Khung chọn danh mục món ăn)
        val spCategory = findViewById<Spinner>(R.id.spFoodCategory)
        val categoryList = arrayOf("Món Chính", "Món Nổi Bật", "Combo Dành Cho Nhóm")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList)
        spCategory.adapter = spinnerAdapter

        // 2. Sự kiện bấm vào hình ảnh để mở thư viện
        imgFood.setOnClickListener {
            pickImageLauncher.launch("image/*") // Mở thư viện ảnh
        }

        // 3. Sự kiện bấm nút Lưu
        btnSave.setOnClickListener {
            val uriString = selectedImageUri?.toString() ?: ""

            // Lấy loại món ăn dựa vào lựa chọn ở Spinner
            val selectedType = when (spCategory.selectedItemPosition) {
                0 -> Food.TYPE_COLLECTION  // "Món Chính"
                1 -> Food.TYPE_FEATURED    // "Món Nổi Bật" (Hãy đảm bảo trong file Food.kt bạn đã khai báo TYPE_FEATURED)
                2 -> Food.TYPE_COMBO       // "Combo" (Hãy đảm bảo trong file Food.kt bạn đã khai báo TYPE_COMBO)
                else -> Food.TYPE_COLLECTION
            }

            // Tạo đối tượng món ăn mới
            val food = Food(
                name = etName.text.toString(),
                price = etPrice.text.toString().toInt(),
                description = etDesc.text.toString(),
                imageResId = R.drawable.padthai, // Vẫn đang tạm lưu hình Padthai mặc định nhé
                viewType = selectedType          // Gán loại món vừa chọn vào đây
            )

            // Lưu vào Database
            db.insertFood(food)

            // Đóng màn hình quay về trang trước
            finish()
        }

        // Sự kiện bấm nút Quay lại
        btnBack.setOnClickListener {
            finish()
        }
    }

    // Hàm xử lý sau khi chọn ảnh từ thư viện xong
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            imgFood.setImageURI(uri) // Hiển thị ảnh vừa chọn lên ImageView
        }
    }
}