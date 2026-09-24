package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_items")
data class InventoryItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // WEAPON, POTION, KEY, ARTIFACT
    val rarity: String,   // COMMON, RARE, EPIC, LEGENDARY, MYTHIC
    val description: String,
    val statBonus: String,
    val quantity: Int = 1,
    val isEquipped: Boolean = false
)
