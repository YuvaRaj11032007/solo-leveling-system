package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val isAwakened: Boolean = false,
    val name: String = "SUNG JIN-WOO",
    val title: String = "E-RANK HUNTER (EVOLVING)",
    val rank: String = "E-Rank",
    val level: Int = 1,
    val currentExp: Int = 0,
    val maxExp: Int = 100,
    val currentHp: Int = 100,
    val maxHp: Int = 100,
    val currentMp: Int = 40,
    val maxMp: Int = 40,
    val fatigue: Int = 5,
    val maxFatigue: Int = 100,
    val strength: Int = 10,
    val agility: Int = 10,
    val vitality: Int = 10,
    val intelligence: Int = 10,
    val perception: Int = 10,
    val unallocatedPoints: Int = 0,
    val gold: Long = 1000L,
    val alarmHour: Int = 7,
    val alarmMinute: Int = 0,
    val alarmEnabled: Boolean = true,
    val penaltyWarningEnabled: Boolean = true
)
