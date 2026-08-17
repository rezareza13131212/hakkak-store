package com.hakkak.store.ui.admin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hakkak.store.R
import com.hakkak.store.model.Product
import com.hakkak.store.repository.StoreRepository

class ManageProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        findViewById<Button>(R.id.btnAddProduct).setOnClickListener {
            val title = findViewById<EditText>(R.id.editProductTitle).text.toString().trim()
            val desc = findViewById<EditText>(R.id.editProductDesc).text.toString().trim()
            val price = findViewById<EditText>(R.id.editProductPrice).text.toString().toLongOrNull()

            if (title.isEmpty() || price == null) {
                Toast.makeText(this, "نام کالا و قیمت را درست وارد کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val product = Product(title = title, description = desc, priceToman = price, active = true)
            StoreRepository.addProduct(product) { success ->
                if (success) {
                    Toast.makeText(this, "کالا اضافه شد", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "خطا در افزودن کالا", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
