package com.example.thaifood

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "ThaiFood.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val createTable = """
            CREATE TABLE foods(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                price INTEGER,
                image INTEGER,
                description TEXT
            )
        """

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertFood(food: Food) {

        val db = writableDatabase
        val values = ContentValues()

        values.put("name", food.name)
        values.put("price", food.price)
        values.put("image", food.imageResId)
        values.put("description", food.description)

        db.insert("foods", null, values)
    }

    fun getAllFoods(): List<Food> {

        val list = mutableListOf<Food>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM foods", null)

        while (cursor.moveToNext()) {

            val food = Food(
                name = cursor.getString(1),
                price = cursor.getInt(2),
                imageResId = cursor.getInt(3),
                description = cursor.getString(4),
                viewType = Food.TYPE_COLLECTION
            )

            list.add(food)
        }

        cursor.close()

        return list
    }

    fun searchFood(keyword: String): ArrayList<Food> {

        val list = ArrayList<Food>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM foods WHERE name LIKE ?",
            arrayOf("%$keyword%")
        )

        if (cursor.moveToFirst()) {
            do {

                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                val image = cursor.getInt(cursor.getColumnIndexOrThrow("image"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                list.add(
                    Food(
                        name,
                        price,
                        image,
                        description,
                        Food.TYPE_COLLECTION
                    )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }
}