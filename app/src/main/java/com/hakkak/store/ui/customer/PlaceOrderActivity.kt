package com.hakkak.store.ui.customer

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.hakkak.store.R
import com.hakkak.store.model.Product
import com.hakkak.store.repository.StoreRepository

class PlaceOrderActivity : AppCompatActivity() {

    private var receiptUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                receiptUri = uri
                Glide.with(
