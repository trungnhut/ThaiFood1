package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var etSearch: EditText

    // Khai báo đủ 3 danh sách
    private lateinit var rvCollection: RecyclerView
    private lateinit var rvFeatured: RecyclerView
    private lateinit var rvCombo: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DatabaseHelper(this)
        etSearch = findViewById(R.id.etSearch)

        if (db.getAllFoods().isEmpty()) {
            insertSampleFood()
        }


        rvCollection = findViewById(R.id.rvCollection)
        rvFeatured = findViewById(R.id.rvFeaturedFood)
        rvCombo = findViewById(R.id.rvCombo)


        rvCollection.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFeatured.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCombo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        loadFoods()

        // Tìm kiếm
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString()
                if (keyword.isEmpty()) {
                    loadFoods()
                } else {
                    val foods = db.searchFood(keyword)
                    // Khi tìm kiếm, tạm thời đổ kết quả vào rvCollection
                    rvCollection.adapter = FoodAdapter(foods) { clickedItem ->
                        openDetail(clickedItem)
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val btnAdd = findViewById<Button>(R.id.btnAddFood)
        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddFoodActivity::class.java))
        }

        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val role = sharedPref.getInt("role", 0)

        if (role == 0) {
            btnAdd.visibility = View.GONE
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_orders -> {
                    if (role == 1) {
                        startActivity(Intent(this, AdminOrderActivity::class.java))
                    } else {
                        startActivity(Intent(this, CartActivity::class.java))
                    }
                    false
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadFoods()
    }


    private fun loadFoods() {
        val allFoods = db.getAllFoods()


        val collectionFoods = allFoods.filter { it.viewType == Food.TYPE_COLLECTION }
        rvCollection.adapter = FoodAdapter(collectionFoods) { openDetail(it) }

        val featuredFoods = allFoods.filter { it.viewType == Food.TYPE_FEATURED }
        rvFeatured.adapter = FoodAdapter(featuredFoods) { openDetail(it) }

        val comboFoods = allFoods.filter { it.viewType == Food.TYPE_COMBO }
        rvCombo.adapter = FoodAdapter(comboFoods) { openDetail(it) }
    }


    private fun insertSampleFood() {
        db.insertFood(Food("Súp Tom Yum", 65000, R.drawable.suptomyum, "Súp Tom Yum", Food.TYPE_COLLECTION))
        db.insertFood(Food("Ba Khía", 50000, R.drawable.bakhia, "Ba Khía", Food.TYPE_COLLECTION))
        db.insertFood(Food("Gỏi Tôm Xoài", 60000, R.drawable.goitomxoai, "Gỏi Tôm Xoài", Food.TYPE_COLLECTION))
        db.insertFood(Food("Lạp Bò", 70000, R.drawable.labbo, "Lạp Bò", Food.TYPE_COLLECTION))
        db.insertFood(Food("Sườn Cay", 105000, R.drawable.suoncay, "Sườn Cay", Food.TYPE_COLLECTION))
        db.insertFood(Food("Pad Thai", 55000, R.drawable.padthai, "Pad Thai", Food.TYPE_COLLECTION))

        // Thêm vài món mẫu cho Nổi bật và Combo để bạn dễ test
        db.insertFood(Food("Xôi Xoài", 65000, R.drawable.padthai, "Tráng miệng ngon", Food.TYPE_FEATURED))
        db.insertFood(Food("Combo Lẩu Thái", 250000, R.drawable.suptomyum, "Dành cho 4 người", Food.TYPE_COMBO))
    }

    private fun openDetail(item: Food) {
        val intent = Intent(this, FoodDetailActivity::class.java)
        intent.putExtra("FOOD_NAME", item.name)
        intent.putExtra("FOOD_PRICE", item.price)
        intent.putExtra("FOOD_IMAGE", item.imageResId)
        intent.putExtra("FOOD_DESC", item.description)
        startActivity(intent)
    }
}