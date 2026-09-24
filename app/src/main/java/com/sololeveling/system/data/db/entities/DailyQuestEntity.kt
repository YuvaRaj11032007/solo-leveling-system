package com.sololeveling.system.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quests")
data class DailyQuestEntity(
    @PrimaryKey val id: Int = 1,
    val questDate: String = "",
    val questName: String = "DAILY TRAINING: PREPARATION TO BECOME STRONG",
    val pushupsCurrent: Int = 0,
    val pushupsTarget: Int = 100,
    val situpsCurrent: Int = 0,
    val situpsTarget: Int = 100,
    val squatsCurrent: Int = 0,
    val squatsTarget: Int = 100,
    val stepsCurrent: Int = 0,
    val stepsTarget: Int = 10000,
    val isCompleted: Boolean = false,
    val isRewardClaimed: Boolean = false,
    val deadlineMillis: Long = 0L
)
