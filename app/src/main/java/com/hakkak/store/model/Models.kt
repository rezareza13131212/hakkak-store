package com.hakkak.store.model

data class Product(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priceToman: Long = 0,
    val active: Boolean = true
)

enum class OrderStatus { PENDING, APPROVED, REJECTED }

data class Order(
    val id: String = "",
    val productId: String = "",
    val productTitle: String = "",
    val customerUid: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val receiptImageUrl: String = "",
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val reviewedAt: Long? = null,
    val rejectionReason: String = "",
    val panelAddress: String = "",
    val panelUsername: String = "",
    val panelPassword: String = ""
)
