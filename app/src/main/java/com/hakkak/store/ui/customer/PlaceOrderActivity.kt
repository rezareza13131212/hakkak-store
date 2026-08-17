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

        findViewById<Button>(R.id.btnSubmitOrder).setOnClickListener {
            val uri = receiptUri
            if (uri == null) {
                Toast.makeText(this, "لطفاً عکس رسید را انتخاب کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val name = findViewById<EditText>(R.id.editCustomerName).text.toString().trim()
            val phone = findViewById<EditText>(R.id.editCustomerPhone).text.toString().trim()
            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "نام و شماره تماس را وارد کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val product = Product(id = productId, title = productTitle, priceToman = productPrice)
            StoreRepository.submitOrder(product, "uid", name, phone, uri) { success, _ ->
                if (success) {
                    Toast.makeText(this, "سفارش ثبت شد", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}
