package com.tranthephong.fooddeliveryapp.Model

data class Order(
    val orderId: String = "",
    val items: List<ItemsModel> = emptyList(),
    val totalPrice: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
