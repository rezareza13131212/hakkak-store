package com.hakkak.store.ui.customer

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hakkak.store.R
import com.hakkak.store.model.Product
import com.hakkak.store.repository.StoreRepository

class PlaceOrderActivity : AppCompatActivity() {

    private var receiptUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                receiptUri = uri
                findViewById<ImageView>(R.id.imgReceiptPreview).setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)

        val productId = intent.getStringExtra("productId") ?: return
        val productTitle = intent.getStringExtra("productTitle") ?: ""
        val productPrice = intent.getLongExtra("productPrice", 0)

        findViewById<TextView>(R.id.txtOrderProductTitle).text = productTitle
        findViewById<TextView>(R.id.txtOrderProductPrice).text = "$productPrice تومان"

        findViewById<Button>(R.id.btnPickReceipt).setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        findViewById
