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

            val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)

            btnRegister.setOnClickListener {

                val fullName = etFullName.text.toString()
                val email = etEmail.text.toString()
                val phone = etPhone.text.toString()
                val password = etPassword.text.toString()

                if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                } else {

                    // Lưu dữ liệu
                    val editor = sharedPref.edit()
                    editor.putString("email", email)
                    editor.putString("password", password)
                    editor.apply()

                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()

                    // 👉 Chuyển qua Login
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}