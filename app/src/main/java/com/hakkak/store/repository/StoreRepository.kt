package com.hakkak.store.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.hakkak.store.model.Order
import com.hakkak.store.model.OrderStatus
import com.hakkak.store.model.Product
import java.util.UUID

object StoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun listActiveProducts(onResult: (List<Product>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("products")
            .whereEqualTo("active", true)
            .get()
            .addOnSuccessListener { snap ->
                onResult(snap.documents.mapNotNull { it.toObject(Product::class.java)?.copy(id = it.id) })
            }
            .addOnFailureListener(onError)
    }

    fun addProduct(product: Product, onDone: (Boolean) -> Unit) {
        db.collection("products").add(product)
            .addOnSuccessListener { onDone(true) }
            .addOnFailureListener { onDone(false) }
    }

    fun submitOrder(
        product: Product,
        customerUid: String,
        customerName: String,
        customerPhone: String,
        receiptImageUri: Uri,
        onDone: (Boolean, String?) -> Unit
    ) {
        val receiptRef = storage.reference.child("receipts/${UUID.randomUUID()}.jpg")
        receiptRef.putFile(receiptImageUri)
            .addOnSuccessListener {
                receiptRef.downloadUrl.addOnSuccessListener { url ->
                    val order = Order(
                        productId = product.id,
                        productTitle = product.title,
                        customerUid = customerUid,
                        customerName = customerName,
                        customerPhone = customerPhone,
                        receiptImageUrl = url.toString(),
                        status = OrderStatus.PENDING
                    )
                    db.collection("orders").add(order)
                        .addOnSuccessListener { onDone(true, null) }
                        .addOnFailureListener { e -> onDone(false, e.message) }
                }
            }
            .addOnFailureListener { e -> onDone(false, e.message) }
    }

    fun listenMyOrders(customerUid: String, onChange: (List<Order>) -> Unit): ListenerRegistration {
        return db.collection("orders")
            .whereEqualTo("customerUid", customerUid)
            .addSnapshotListener { snap, _ ->
                val orders = snap?.documents?.mapNotNull {
                    it.toObject(Order::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onChange(orders.sortedByDescending { it.createdAt })
            }
    }

    fun listenPendingOrders(onChange: (List<Order>) -> Unit): ListenerRegistration {
        return db.collection("orders")
            .whereEqualTo("status", OrderStatus.PENDING.name)
            .addSnapshotListener { snap, _ ->
                val orders = snap?.documents?.mapNotNull {
                    it.toObject(Order::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onChange(orders.sortedBy { it.createdAt })
            }
    }

    fun approveOrder(
        orderId: String,
        panelAddress: String,
        panelUsername: String,
        panelPassword: String,
        onDone: (Boolean) -> Unit
    ) {
        db.collection("orders").document(orderId)
            .update(mapOf(
                "status" to OrderStatus.APPROVED.name,
                "panelAddress" to panelAddress,
                "panelUsername" to panelUsername,
                "panelPassword" to panelPassword,
                "reviewedAt" to System.currentTimeMillis()
            ))
            .addOnSuccessListener { onDone(true) }
            .addOnFailureListener { onDone(false) }
    }

    fun rejectOrder(orderId: String, reason: String, onDone: (Boolean) -> Unit) {
        db.collection("orders").document(orderId)
            .update(mapOf(
                "status" to OrderStatus.REJECTED.name,
                "rejectionReason" to reason,
                "reviewedAt" to System.currentTimeMillis()
            ))
            .addOnSuccessListener { onDone(true) }
            .addOnFailureListener { onDone(false) }
    }

    fun checkIsAdmin(uid: String, onResult: (Boolean) -> Unit) {
        db.collection("admins").document(uid).get()
            .addOnSuccessListener { onResult(it.exists()) }
            .addOnFailureListener { onResult(false) }
    }
}
