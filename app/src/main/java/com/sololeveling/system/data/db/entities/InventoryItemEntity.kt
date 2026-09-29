package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_items")
data class InventoryItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // FOCUS, RECOVERY, GEAR, TOOL
    val rarity: String,   // STANDARD, PRO, ELITE, PINNACLE
    val description: String,
    val statBonus: String,
    val quantity: Int = 1,
    val isEquipped: Boolean = false
)
