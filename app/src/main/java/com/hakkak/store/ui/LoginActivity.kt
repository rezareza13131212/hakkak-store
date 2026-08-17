package com.hakkak.store.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hakkak.store.R
import com.hakkak.store.repository.StoreRepository
import com.hakkak.store.ui.admin.AdminOrdersActivity
import com.hakkak.store.ui.customer.ProductListActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.editEmail)
        val passField = findViewById<EditText>(R.id.editPassword)

        findViewById
