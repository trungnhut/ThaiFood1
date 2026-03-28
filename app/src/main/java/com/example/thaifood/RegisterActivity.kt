package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmailRegister)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvGoLogin = findViewById<TextView>(R.id.tvGoLogin)

        tvGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gọi Database để lưu người dùng mới
            val db = DatabaseHelper(this)
            val isSuccess = db.registerUser(fullName, email, phone, password)

            if (isSuccess) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Đăng ký thất bại! Email có thể đã tồn tại.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}