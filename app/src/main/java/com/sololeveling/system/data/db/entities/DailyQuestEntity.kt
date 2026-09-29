package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quests")
data class DailyQuestEntity(
    @PrimaryKey val id: Int = 1,
    val questDate: String = "",
    val questName: String = "DAILY DISCIPLINE PROTOCOL: HUMAN PERFORMANCE OPTIMIZATION",
    // Physical Training
    val pushupsCurrent: Int = 0,
    val pushupsTarget: Int = 50,
    val situpsCurrent: Int = 0,
    val situpsTarget: Int = 50,
    val squatsCurrent: Int = 0,
    val squatsTarget: Int = 50,
    val stepsCurrent: Int = 0,
    val stepsTarget: Int = 8000,
    // Realistic Lifestyle & Cognitive Tasks
    val waterCurrentMl: Int = 0,
    val waterTargetMl: Int = 3000,
    val deepWorkCurrentMins: Int = 0,
    val deepWorkTargetMins: Int = 60,
    val readingCurrentMins: Int = 0,
    val readingTargetMins: Int = 20,
    // Status
    val isCompleted: Boolean = false,
    val isRewardClaimed: Boolean = false,
    val deadlineMillis: Long = 0L
)
