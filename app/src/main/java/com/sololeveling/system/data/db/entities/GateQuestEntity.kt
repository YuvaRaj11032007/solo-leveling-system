package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gate_quests")
data class GateQuestEntity(
    @PrimaryKey val id: Int,
    val gateRank: String,
    val title: String,
    val description: String,
    val enemyName: String,
    val enemyHp: Int,
    val enemyMaxHp: Int,
    val workoutObjective: String,
    val expReward: Int,
    val goldReward: Long,
    val itemRewardName: String,
    val isCleared: Boolean = false,
    val isAvailable: Boolean = true
)
