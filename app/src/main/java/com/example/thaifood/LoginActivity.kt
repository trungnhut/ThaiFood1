package com.example.thaifood

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmailLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoRegister = findViewById<TextView>(R.id.tvGoRegister)
        val cbRemember = findViewById<CheckBox>(R.id.cbRemember)

        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)


        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editor = sharedPref.edit()
            var loginSuccess = false


            if (email == "admin" && password == "admin") {
                editor.putInt("role", 1)
                editor.putString("fullName", "Quản trị viên")
                loginSuccess = true
            } else {

                val db = DatabaseHelper(this)
                val userFromDB = db.loginUser(email, password)

                if (userFromDB != null) {

                    editor.putInt("role", userFromDB.second)
                    editor.putString("fullName", userFromDB.first)
                    loginSuccess = true
                }
            }

            if (loginSuccess) {
                if (cbRemember.isChecked) {
                    editor.putBoolean("isLoggedIn", true)
                }
                editor.apply()
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
            }
        }

        tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}