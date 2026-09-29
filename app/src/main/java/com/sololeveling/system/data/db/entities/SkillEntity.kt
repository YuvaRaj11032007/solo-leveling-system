package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hunter_skills")
data class SkillEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // PASSIVE, ACTIVE, PROTOCOL
    val mpCost: Int,  // Mental Bandwidth / Focus cost
    val level: Int = 1,
    val description: String,
    val cooldownSeconds: Int = 0,
    val isUnlocked: Boolean = false
)
