package com.hakkak.store.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hakkak.store.R
import com.hakkak.store.model.Order
import com.hakkak.store.repository.StoreRepository

class AdminOrdersActivity : AppCompatActivity() {

    private var registration: AutoCloseable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_orders)

        val recycler = findViewById<RecyclerView>(R.id.recyclerAdminOrders)
        recycler.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnManageProducts).setOnClickListener {
            startActivity(Intent(this, ManageProductsActivity::class.java))
        }

        registration = StoreRepository.listenPendingOrders { orders ->
            recycler.adapter = AdminOrderAdapter(orders) { order ->
                val intent = Intent(this, OrderReviewActivity::class.java)
                intent.putExtra("orderId", order.id)
                intent.putExtra("productTitle", order.productTitle)
                intent.putExtra("customerName", order.customerName)
                intent.putExtra("customerPhone", order.customerPhone)
                intent.putExtra("receiptImageUrl", order.receiptImageUrl)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        registration?.close()
        super.onDestroy()
    }
}

class AdminOrderAdapter(
    private val items: List<Order>,
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<AdminOrderAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtAdminOrderTitle)
        val customer: TextView = view.findViewById(R.id.txtAdminOrderCustomer)
        val reviewBtn: Button = view.findViewById(R.id.btnReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_order, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val o = items[position]
        holder.title.text = o.productTitle
        holder.customer.text = "${o.customerName} - ${o.customerPhone}"
        holder.reviewBtn.setOnClickListener { onClick(o) }
    }

    override fun getItemCount() = items.size
}
