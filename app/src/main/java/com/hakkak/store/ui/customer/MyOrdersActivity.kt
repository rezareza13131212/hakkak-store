package com.hakkak.store.ui.customer

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hakkak.store.R
import com.hakkak.store.model.Order
import com.hakkak.store.model.OrderStatus
import com.hakkak.store.repository.StoreRepository

class MyOrdersActivity : AppCompatActivity() {

    private var registration: AutoCloseable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        val recycler = findViewById<RecyclerView>(R.id.recyclerMyOrders)
        recycler.layoutManager = LinearLayoutManager(this)

        registration = StoreRepository.listenMyOrders("uid") { orders ->
            recycler.adapter = MyOrderAdapter(orders)
        }
    }

    override fun onDestroy() {
        registration?.close()
        super.onDestroy()
    }
}

class MyOrderAdapter(private val items: List<Order>) :
    RecyclerView.Adapter<MyOrderAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtOrderItemTitle)
        val status: TextView = view.findViewById(R.id.txtOrderItemStatus)
        val credentials: TextView = view.findViewById(R.id.txtOrderItemCredentials)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_order, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val o = items[position]
        holder.title.text = o.productTitle
        holder.status.text = when (o.status) {
            OrderStatus.PENDING -> "در انتظار تایید"
            OrderStatus.APPROVED -> "تایید شده ✅"
            OrderStatus.REJECTED -> "رد شده ❌"
        }
        if (o.status == OrderStatus.APPROVED) {
            holder.credentials.visibility = View.VISIBLE
            holder.credentials.text =
                "آدرس: ${o.panelAddress}\nیوزر: ${o.panelUsername}\nپسورد: ${o.panelPassword}"
        } else {
            holder.credentials.visibility = View.GONE
        }
    }

    override fun getItemCount() = items.size
    }
