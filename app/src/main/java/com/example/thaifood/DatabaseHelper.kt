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
                description TEXT,
                viewType INTEGER ,
                imageUri TEXT
            )
        """
        db.execSQL(createTable)

        val createOrderTable = """
        CREATE TABLE orders(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            customerName TEXT,
            details TEXT,
            totalPrice INTEGER,
            status INTEGER
        )
        """
        db.execSQL(createOrderTable)

        val createUserTable = """
        CREATE TABLE users(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            fullName TEXT,
            email TEXT UNIQUE,
            phone TEXT,
            password TEXT,
            role INTEGER
        )
        """
        db.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertFood(food: Food) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", food.name)
        values.put("price", food.price)
        values.put("image", food.imageResId)
        values.put("description", food.description)
        values.put("viewType", food.viewType)
        values.put("imageUri", food.imageUri)
        db.insert("foods", null, values)
        db.close()
    }

    fun updateFood(food: Food, originalName: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", food.name)
            put("price", food.price)
            put("image", food.imageResId)
            put("description", food.description)
            put("viewType", food.viewType)
            put("imageUri", food.imageUri)
        }
        db.update("foods", values, "name=?", arrayOf(originalName))
        db.close()
    }

    fun getAllFoods(): List<Food> {
        val list = mutableListOf<Food>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM foods", null)

        while (cursor.moveToNext()) {
            val food = Food(
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("image")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                viewType = cursor.getInt(cursor.getColumnIndexOrThrow("viewType")),
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow("imageUri")) ?: ""
            )

            list.add(food)
        }
        cursor.close()
        return list
    }

    fun searchFood(keyword: String): ArrayList<Food> {
        val list = ArrayList<Food>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM foods WHERE name LIKE ?", arrayOf("%$keyword%"))

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                val image = cursor.getInt(cursor.getColumnIndexOrThrow("image"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val viewType = cursor.getInt(cursor.getColumnIndexOrThrow("viewType"))
                list.add(Food(name, price, image, description, viewType))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun insertOrder(order: Order): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put("customerName", order.customerName)
        values.put("details", order.details)
        values.put("totalPrice", order.totalPrice)
        values.put("status", order.status)
        val id = db.insert("orders", null, values)
        db.close()
        return id
    }

    fun getAllOrders(): List<Order> {
        val list = mutableListOf<Order>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM orders ORDER BY id DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val order = Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    customerName = cursor.getString(cursor.getColumnIndexOrThrow("customerName")),
                    details = cursor.getString(cursor.getColumnIndexOrThrow("details")),
                    totalPrice = cursor.getInt(cursor.getColumnIndexOrThrow("totalPrice")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                )
                list.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun updateOrderStatus(orderId: Int, newStatus: Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("status", newStatus)
        db.update("orders", values, "id = ?", arrayOf(orderId.toString()))
        db.close()
    }

    fun deleteFoodByName(foodName: String) {
        val db = this.writableDatabase
        db.delete("foods", "name=?", arrayOf(foodName))
        db.close()
    }


    fun registerUser(fullName: String, email: String, phone: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("fullName", fullName)
        values.put("email", email)
        values.put("phone", phone)
        values.put("password", password)
        values.put("role", 0)

        val result = db.insert("users", null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(email: String, password: String): Pair<String, Int>? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT fullName, role FROM users WHERE email=? AND password=?", arrayOf(email, password))

        var userInfo: Pair<String, Int>? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(0)
            val role = cursor.getInt(1)
            userInfo = Pair(name, role)
        }
        cursor.close()
        return userInfo
    }
    fun getCanceledOrders(role: Int, customerName: String): List<Order> {
        val list = mutableListOf<Order>()
        val db = readableDatabase

        val cursor = if (role == 1) {
            db.rawQuery("SELECT * FROM orders WHERE status = 1 ORDER BY id DESC", null)
        } else {
            db.rawQuery("SELECT * FROM orders WHERE status = 1 AND customerName = ? ORDER BY id DESC", arrayOf(customerName))
        }

        if (cursor.moveToFirst()) {
            do {
                val order = Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    customerName = cursor.getString(cursor.getColumnIndexOrThrow("customerName")),
                    details = cursor.getString(cursor.getColumnIndexOrThrow("details")),
                    totalPrice = cursor.getInt(cursor.getColumnIndexOrThrow("totalPrice")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                )
                list.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
    fun getOrderById(id: Int): Order? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM orders WHERE id = ?", arrayOf(id.toString()))
        var order: Order? = null
        if (cursor.moveToFirst()) {
            order = Order(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                customerName = cursor.getString(cursor.getColumnIndexOrThrow("customerName")),
                details = cursor.getString(cursor.getColumnIndexOrThrow("details")),
                totalPrice = cursor.getInt(cursor.getColumnIndexOrThrow("totalPrice")),
                status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
            )
        }
        cursor.close()
        return order
    }
}