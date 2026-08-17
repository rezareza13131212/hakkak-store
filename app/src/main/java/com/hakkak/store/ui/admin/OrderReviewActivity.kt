package com.hakkak.store.ui.admin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hakkak.store.R
import com.hakkak.store.repository.StoreRepository

class OrderReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_review)

        val orderId = intent.getStringExtra("orderId") ?: return
        val productTitle = intent.getStringExtra("productTitle") ?: ""
        val customerName = intent.getStringExtra("customerName") ?: ""
        val customerPhone = intent.getStringExtra("customerPhone") ?: ""

        findViewById<TextView>(R.id.txtReviewProduct).text = productTitle
        findViewById<TextView>(R.id.txtReviewCustomer).text = "$customerName - $customerPhone"

        val panelAddressField = findViewById<EditText>(R.id.editPanelAddress)
        val panelUserField = findViewById<EditText>(R.id.editPanelUsername)
        val panelPassField = findViewById<EditText>(R.id.editPanelPassword)

        findViewById<Button>(R.id.btnApprove).setOnClickListener {
            val addr = panelAddressField.text.toString().trim()
            val user = panelUserField.text.toString().trim()
            val pass = panelPassField.text.toString().trim()
            if (addr.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "همه فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            StoreRepository.approveOrder(orderId, addr, user, pass) { success ->
                if (success) {
                    Toast.makeText(this, "تایید شد", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        findViewById<Button>(R.id.btnReject).setOnClickListener {
            StoreRepository.rejectOrder(orderId, "رسید نامعتبر") { success ->
                if (success) {
                    Toast.makeText(this, "رد شد", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
