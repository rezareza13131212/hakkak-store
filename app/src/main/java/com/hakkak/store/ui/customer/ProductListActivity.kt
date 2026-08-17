package com.hakkak.store.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hakkak.store.R
import com.hakkak.store.model.Product
import com.hakkak.store.repository.StoreRepository

class ProductListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val recycler = findViewById<RecyclerView>(R.id.recyclerProducts)
        recycler.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnMyOrders).setOnClickListener {
            startActivity(Intent(this, MyOrdersActivity::class.java))
        }

        StoreRepository.listActiveProducts(
            onResult = { products ->
                recycler.adapter = ProductAdapter(products) { product ->
                    val intent = Intent(this, PlaceOrderActivity::class.java)
                    intent.putExtra("productId", product.id)
                    intent.putExtra("productTitle", product.title)
                    intent.putExtra("productPrice", product.priceToman)
                    startActivity(intent)
                }
            },
            onError = {}
        )
    }
}

class ProductAdapter(
    private val items: List<Product>,
    private val onBuyClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtProductTitle)
        val desc: TextView = view.findViewById(R.id.txtProductDesc)
        val price: TextView = view.findViewById(R.id.txtProductPrice)
        val buyBtn: Button = view.findViewById(R.id.btnBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.title.text = p.title
        holder.desc.text = p.description
        holder.price.text = "${p.priceToman} تومان"
        holder.buyBtn.setOnClickListener { onBuyClick(p) }
    }

    override fun getItemCount() = items.size
}
