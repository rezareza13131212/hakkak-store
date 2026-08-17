package com.hakkak.store.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ListenerRegistration
import com.hakkak.store.R
import com.hakkak.store.model.Order
import com.hakkak.store.repository.StoreRepository

class AdminOrdersActivity : AppCompatActivity() {

    private var registration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_orders)

        val recycler = findViewById
