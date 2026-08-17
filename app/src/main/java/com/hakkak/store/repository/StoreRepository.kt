package com.hakkak.store.repository

import android.net.Uri
import com.hakkak.store.model.Order
import com.hakkak.store.model.OrderStatus
import com.hakkak.store.model.Product

object StoreRepository {

    fun listActiveProducts(onResult: (List<Product>) -> Unit, onError: (Exception) -> Unit) {
        onResult(emptyList())
    }

    fun addProduct(product: Product, onDone: (Boolean) -> Unit) {
        onDone(true)
    }

    fun submitOrder(
        product: Product,
        customerUid: String,
        customerName: String,
        customerPhone: String,
        receiptImageUri: Uri,
        onDone: (Boolean, String?) -> Unit
    ) {
        onDone(true, null)
    }

    fun listenMyOrders(customerUid: String, onChange: (List<Order>) -> Unit): AutoCloseable {
        onChange(emptyList())
        return AutoCloseable {}
    }

    fun listenPendingOrders(onChange: (List<Order>) -> Unit): AutoCloseable {
        onChange(emptyList())
        return AutoCloseable {}
    }

    fun approveOrder(orderId: String, panelAddress: String, panelUsername: String, panelPassword: String, onDone: (Boolean) -> Unit) {
        onDone(true)
    }

    fun rejectOrder(orderId: String, reason: String, onDone: (Boolean) -> Unit) {
        onDone(true)
    }

    fun checkIsAdmin(uid: String, onResult: (Boolean) -> Unit) {
        onResult(false)
    }
}
