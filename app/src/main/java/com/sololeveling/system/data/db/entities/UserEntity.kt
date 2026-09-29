package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sololeveling.system.data.gemini.GeminiService

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val isAwakened: Boolean = false,
    val name: String = "OPERATOR",
    val title: String = "TIER-E CANDIDATE (DISCIPLINE BASELINE)",
    val rank: String = "E-Rank",
    val level: Int = 1,
    val currentExp: Int = 0,
    val maxExp: Int = 100,
    val currentHp: Int = 100,
    val maxHp: Int = 100,
    val currentMp: Int = 50, // Represents Mental Focus / Cognitive Bandwidth
    val maxMp: Int = 50,
    val fatigue: Int = 0,
    val maxFatigue: Int = 100,
    val strength: Int = 10,     // Physical Power & Resistance
    val agility: Int = 10,      // Cardio, Speed, Agility
    val vitality: Int = 10,     // Health, Immunity, Recovery
    val intelligence: Int = 10, // Cognitive Focus & Deep Work
    val perception: Int = 10,   // Mindfulness, Discipline, Awareness
    val unallocatedPoints: Int = 0,
    val gold: Long = 1000L,     // Merit / Achievement Credits
    val alarmHour: Int = 7,
    val alarmMinute: Int = 0,
    val alarmEnabled: Boolean = true,
    val penaltyWarningEnabled: Boolean = true,
    val geminiApiKey: String = GeminiService.DEFAULT_API_KEY,
    val streakDays: Int = 1
)
