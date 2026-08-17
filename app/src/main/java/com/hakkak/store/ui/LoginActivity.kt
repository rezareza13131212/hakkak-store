package com.hakkak.store.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hakkak.store.R
import com.hakkak.store.repository.StoreRepository
import com.hakkak.store.ui.admin.AdminOrdersActivity
import com.hakkak.store.ui.customer.ProductListActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField = findViewById<EditText>(R.id.editEmail)
        val passField = findViewById<EditText>(R.id.editPassword)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.contains("admin")) {
                startActivity(Intent(this, AdminOrdersActivity::class.java))
            } else {
                startActivity(Intent(this, ProductListActivity::class.java))
            }
            finish()
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
            finish()
        }
    }
}
