package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hunter_skills")
data class SkillEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // PASSIVE, ACTIVE, ULTIMATE
    val mpCost: Int,
    val level: Int = 1,
    val description: String,
    val cooldownSeconds: Int = 0,
    val isUnlocked: Boolean = false
)
