package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var rvCollection: RecyclerView
    private lateinit var etSearch: EditText

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

        // ô tìm kiếm
        etSearch = findViewById(R.id.etSearch)

        // nếu database chưa có dữ liệu → thêm dữ liệu mẫu
        if (db.getAllFoods().isEmpty()) {
            insertSampleFood()
        }

        rvCollection = findViewById(R.id.rvCollection)
        rvCollection.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        loadFoods()

        // sự kiện tìm kiếm
        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                val keyword = s.toString()

                if (keyword.isEmpty()) {
                    loadFoods()
                } else {

                    val foods = db.searchFood(keyword)

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

        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_orders -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
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
        val foods = db.getAllFoods()

        rvCollection.adapter = FoodAdapter(foods) { clickedItem ->
            openDetail(clickedItem)
        }
    }

    private fun insertSampleFood() {

        db.insertFood(
            Food("Súp Tom Yum",65000,R.drawable.suptomyum,"Súp Tom Yum",Food.TYPE_COLLECTION)
        )

        db.insertFood(
            Food("Ba Khía",50000,R.drawable.bakhia,"Ba Khía",Food.TYPE_COLLECTION)
        )

        db.insertFood(
            Food("Gỏi Tôm Xoài",60000,R.drawable.goitomxoai,"Gỏi Tôm Xoài",Food.TYPE_COLLECTION)
        )

        db.insertFood(
            Food("Lạp Bò",70000,R.drawable.labbo,"Lạp Bò",Food.TYPE_COLLECTION)
        )

        db.insertFood(
            Food("Sườn Cay",105000,R.drawable.suoncay,"Sườn Cay",Food.TYPE_COLLECTION)
        )

        db.insertFood(
            Food("Pad Thai",55000,R.drawable.padthai,"Pad Thai",Food.TYPE_COLLECTION)
        )
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