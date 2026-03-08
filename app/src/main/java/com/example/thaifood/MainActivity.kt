package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //BỘ SƯU TẬP
        val collections = listOf(
            Food(name = "Súp Tom Yum", price = 65000, imageResId = R.drawable.suptomyum, promoText = "Súp TomYum", viewType = Food.TYPE_COLLECTION),
            Food(name = "Xôi Xoài", price = 45000, imageResId = R.drawable.xoixoai, promoText = "Gỏi Xoài", viewType = Food.TYPE_COLLECTION),
            Food(name = "Ba Khía", price = 50000, imageResId = R.drawable.bakhia, promoText = "Ba Khía", viewType = Food.TYPE_COLLECTION),
            Food(name = "Gỏi Tôm Xoài", price = 60000, imageResId = R.drawable.goitomxoai, promoText = "Gỏi Tôm Xoài", viewType = Food.TYPE_COLLECTION),
            Food(name = "Lạp Bò", price = 70000, imageResId = R.drawable.labbo, promoText = "Lạp Bò", viewType = Food.TYPE_COLLECTION),
            Food(name = "Sườn Cay", price = 105000, imageResId = R.drawable.suoncay, promoText = "Sườn Cay", viewType = Food.TYPE_COLLECTION)
        )

        val rvCollection = findViewById<RecyclerView>(R.id.rvCollection)
        rvCollection.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvCollection.adapter = FoodAdapter(collections) { clickedItem ->
            openDetail(clickedItem)
        }

        // MÓN NỔI BẬT
        val featuredFoods = listOf(
            Food(name = "Pad Thai", price = 55000, imageResId = R.drawable.padthai, viewType = Food.TYPE_FEATURED),
            Food(name = "Tom Yum", price = 65000, imageResId = R.drawable.suptomyum, viewType = Food.TYPE_FEATURED)
        )

        val rvFeaturedFood = findViewById<RecyclerView>(R.id.rvFeaturedFood)
        rvFeaturedFood.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFeaturedFood.adapter = FoodAdapter(featuredFoods) { clickedItem ->
            openDetail(clickedItem)
        }

        // COMBO
        val combos = listOf(
            Food(
                name = "COMBO 1",
                price = 105000,
                imageResId = R.drawable.combo1,
                viewType = Food.TYPE_COMBO,
                description = "Gồm: 1 Súp Tom Yum, 1 Pad Thai, 2 Trà Sữa Thái" // Thêm chi tiết món
            ),
            Food(
                name = "COMBO 2",
                price = 105000,
                imageResId = R.drawable.combo2,
                viewType = Food.TYPE_COMBO,
                description = "Gồm: 1 Sườn Cay, 1 Xôi Xoài, 2 Nước Ngọt" // Thêm chi tiết món
            )
        )

        val rvCombo = findViewById<RecyclerView>(R.id.rvCombo)
        rvCombo.layoutManager = LinearLayoutManager(this) // Combo cuộn dọc
        rvCombo.adapter = FoodAdapter(combos) { clickedItem ->
            openDetail(clickedItem)
        }

        //GIỎ HÀNG
        val imgCart = findViewById<ImageView>(R.id.imgCart)
        imgCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
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