package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Real-World High-Performance Challenge / Milestone Operation.
 * Replaces fantasy dungeon gates with science-backed, high-impact athletic and cognitive protocols.
 */
@Entity(tableName = "gate_quests")
data class GateQuestEntity(
    @PrimaryKey val id: Int,
    val gateRank: String,           // "Tier-D", "Tier-C", "Tier-B", "Tier-A", "Tier-S"
    val title: String,              // e.g. "OPERATION: 5KM AEROBIC THRESHOLD"
    val description: String,        // Science-based performance context
    val enemyName: String,          // Benchmark / Target Milestone (e.g. "5,000 Cadence Paces")
    val enemyHp: Int,               // Remaining Target Units
    val enemyMaxHp: Int,            // Total Target Units
    val workoutObjective: String,   // Exact protocol steps
    val expReward: Int,
    val goldReward: Long,
    val itemRewardName: String,
    val isCleared: Boolean = false,
    val isAvailable: Boolean = true
)
