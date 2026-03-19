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
    fun insertOrder(order: Order) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("customerName", order.customerName)
        values.put("details", order.details)
        values.put("totalPrice", order.totalPrice)
        values.put("status", order.status) // Mặc định là 0 (Chờ xử lý)

        db.insert("orders", null, values)
    }

    // Lấy danh sách tất cả đơn hàng (dành cho Admin)
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

    // Cập nhật trạng thái đơn hàng (Dùng để Hủy đơn)
    fun updateOrderStatus(orderId: Int, newStatus: Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("status", newStatus)
        // Cập nhật dòng có id tương ứng
        db.update("orders", values, "id = ?", arrayOf(orderId.toString()))
    }
}