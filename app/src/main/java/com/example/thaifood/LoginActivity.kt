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

        // Kiểm tra xem đã đăng nhập trước đó chưa (nếu có lưu)
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

            // Kiểm tra 3 tài khoản cứng (Hardcode)
            if (email == "admin" && password == "admin") {
                editor.putInt("role", 1) // 1 là Admin
                editor.putString("fullName", "Quản trị viên")
                loginSuccess = true
            } else if (email == "khach1" && password == "123") {
                editor.putInt("role", 0) // 0 là Khách
                editor.putString("fullName", "Khách hàng 1")
                loginSuccess = true
            } else if (email == "khach2" && password == "123") {
                editor.putInt("role", 0)
                editor.putString("fullName", "Khách hàng 2")
                loginSuccess = true
            } else {
                // Nếu không phải 3 tài khoản cứng, kiểm tra tài khoản đã đăng ký trong SharedPreferences
                val savedEmail = sharedPref.getString("email", "")
                val savedPassword = sharedPref.getString("password", "")

                // Lấy tên người dùng đã lưu lúc đăng ký (nếu có), nếu không có thì lấy tạm email
                val savedFullName = sharedPref.getString("fullName", savedEmail)

                if (email == savedEmail && password == savedPassword && savedEmail!!.isNotEmpty()) {

                    // --- ĐÂY LÀ PHẦN ĐÃ ĐƯỢC SỬA LỖI ---
                    editor.putInt("role", 0) // Ép quyền về 0 (Khách hàng) để xóa "bóng ma" Admin
                    editor.putString("fullName", savedFullName) // Lưu tên hiển thị
                    // -----------------------------------

                    loginSuccess = true
                }
            }

            if (loginSuccess) {
                if (cbRemember.isChecked) {
                    editor.putBoolean("isLoggedIn", true) // Lưu trạng thái đăng nhập
                } else {
                    // Nếu không check Nhớ mật khẩu, ta cũng có thể quyết định không lưu (tùy logic app)
                    // Tạm thời ở đây vẫn cho phép đăng nhập qua phiên này
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