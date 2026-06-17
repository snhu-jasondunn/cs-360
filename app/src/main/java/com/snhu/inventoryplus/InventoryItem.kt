package com.snhu.inventoryplus

data class InventoryItem(
    val id: Long,
    val name: String,
    val quantity: Int,
    val lowAlert: Int,
    val updatedAt: String
)
