package com.sololeveling.system.data.repository

import com.sololeveling.system.data.db.AppDatabase
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.data.db.entities.InventoryItemEntity
import com.sololeveling.system.data.db.entities.SkillEntity
import com.sololeveling.system.data.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SystemRepository(private val db: AppDatabase) {

    val userFlow: Flow<UserEntity?> = db.userDao().getUserFlow()
    val dailyQuestFlow: Flow<DailyQuestEntity?> = db.questDao().getDailyQuestFlow()
    val gatesFlow: Flow<List<GateQuestEntity>> = db.questDao().getAllGatesFlow()
    val inventoryFlow: Flow<List<InventoryItemEntity>> = db.inventoryDao().getAllItemsFlow()
    val skillsFlow: Flow<List<SkillEntity>> = db.skillDao().getAllSkillsFlow()

    suspend fun initializeDefaultDataIfNeeded() {
        val currentUser = db.userDao().getUser()
        if (currentUser == null) {
            val initialUser = UserEntity(
                id = 1,
                isAwakened = false,
                name = "SUNG JIN-WOO",
                title = "E-RANK HUNTER (EVOLVING)",
                rank = "E-Rank",
                level = 1,
                currentExp = 0,
                maxExp = 100,
                currentHp = 100,
                maxHp = 100,
                currentMp = 40,
                maxMp = 40,
                fatigue = 5,
                maxFatigue = 100,
                strength = 10,
                agility = 10,
                vitality = 10,
                intelligence = 10,
                perception = 10,
                unallocatedPoints = 0,
                gold = 1000L,
                alarmHour = 7,
                alarmMinute = 0,
                alarmEnabled = true,
                penaltyWarningEnabled = true
            )
            db.userDao().insertUser(initialUser)
        }

        // Initialize Daily Quest if not present or date changed
        val todayStr = getTodayDateString()
        val currentQuest = db.questDao().getDailyQuest()
        if (currentQuest == null || currentQuest.questDate != todayStr) {
            val user = db.userDao().getUser() ?: UserEntity()
            val targets = calculateTargetsForRankAndLevel(user.rank, user.level)
            val newQuest = DailyQuestEntity(
                id = 1,
                questDate = todayStr,
                questName = "DAILY TRAINING: PREPARATION TO BECOME STRONG",
                pushupsCurrent = 0,
                pushupsTarget = targets.pushups,
                situpsCurrent = 0,
                situpsTarget = targets.situps,
                squatsCurrent = 0,
                squatsTarget = targets.squats,
                stepsCurrent = 0,
                stepsTarget = targets.steps,
                isCompleted = false,
                isRewardClaimed = false,
                deadlineMillis = getMidnightMillis()
            )
            db.questDao().insertOrUpdateDailyQuest(newQuest)
        }

        // Initialize Gates
        val currentGates = db.questDao().getGateById(1)
        if (currentGates == null) {
            val initialGates = listOf(
                GateQuestEntity(
                    id = 1,
                    gateRank = "D-Rank",
                    title = "[OPTIONAL: GATE CLEARANCE]",
                    description = "D-Rank Dungeon: Goblin Den in Gwanak-gu. Exterminate goblin vanguard.",
                    enemyName = "Hobgoblin Chieftain",
                    enemyHp = 100,
                    enemyMaxHp = 100,
                    workoutObjective = "25 Jumping Jacks + 25 High Knees",
                    expReward = 350,
                    goldReward = 2500L,
                    itemRewardName = "Kasaka's Poison Fang",
                    isCleared = false,
                    isAvailable = true
                ),
                GateQuestEntity(
                    id = 2,
                    gateRank = "C-Rank",
                    title = "C-Rank Gate: Cerberus' Lair",
                    description = "Infernal dungeon entrance. Gate of the Underworld Watchdog.",
                    enemyName = "Three-Headed Cerberus",
                    enemyHp = 250,
                    enemyMaxHp = 250,
                    workoutObjective = "40 Mountain Climbers + 30 Lunges",
                    expReward = 750,
                    goldReward = 6000L,
                    itemRewardName = "Demon Monarch's Ring",
                    isCleared = false,
                    isAvailable = true
                ),
                GateQuestEntity(
                    id = 3,
                    gateRank = "B-Rank",
                    title = "B-Rank Gate: Ice Elf Kingdom",
                    description = "Blizzard covered cavern dominated by Baruka's white phantom warriors.",
                    enemyName = "Ice Elf Baruka",
                    enemyHp = 500,
                    enemyMaxHp = 500,
                    workoutObjective = "50 Jump Squats + 35 Burpees",
                    expReward = 1500,
                    goldReward = 15000L,
                    itemRewardName = "Baruka's Dagger",
                    isCleared = false,
                    isAvailable = false
                ),
                GateQuestEntity(
                    id = 4,
                    gateRank = "Red Gate",
                    title = "Red Gate: Shadow Monarch Throne",
                    description = "An isolated high-dimensional rupture. Complete severance from reality.",
                    enemyName = "Blood-Red Commander Igris",
                    enemyHp = 1000,
                    enemyMaxHp = 1000,
                    workoutObjective = "100 Push-ups + 100 Sit-ups in 1 Session",
                    expReward = 5000,
                    goldReward = 50000L,
                    itemRewardName = "Ruler's Authority Core",
                    isCleared = false,
                    isAvailable = false
                )
            )
            db.questDao().insertGates(initialGates)
        }

        // Initialize Inventory Items
        val item1 = db.inventoryDao().getItemById("item_elixir")
        if (item1 == null) {
            val defaultItems = listOf(
                InventoryItemEntity(
                    id = "item_elixir",
                    name = "Full Recovery Elixir",
                    category = "POTION",
                    rarity = "RARE",
                    description = "Instantly clears all player fatigue, restores HP to 100% and MP to 100%.",
                    statBonus = "+100% HP/MP, -100 Fatigue",
                    quantity = 3,
                    isEquipped = false
                ),
                InventoryItemEntity(
                    id = "item_teleport",
                    name = "Dungeon Return Stone",
                    category = "KEY",
                    rarity = "EPIC",
                    description = "Allows an instant emergency escape from any dungeon or penalty zone.",
                    statBonus = "Instant Teleport",
                    quantity = 1,
                    isEquipped = false
                ),
                InventoryItemEntity(
                    id = "item_kasaka_dagger",
                    name = "Kasaka's Poison Fang",
                    category = "WEAPON",
                    rarity = "RARE",
                    description = "A dagger crafted from the venomous fang of the Great Swamp Snake Kasaka.",
                    statBonus = "+25 ATK, +5 AGI (Paralysis Effect)",
                    quantity = 1,
                    isEquipped = true
                ),
                InventoryItemEntity(
                    id = "item_demon_key",
                    name = "Demon Castle Entry Key",
                    category = "KEY",
                    rarity = "MYTHIC",
                    description = "Unlocks the 100-floor Demon King Baran tower dungeon.",
                    statBonus = "Special Raid Access",
                    quantity = 1,
                    isEquipped = false
                )
            )
            db.inventoryDao().insertItems(defaultItems)
        }

        // Initialize Skills
        val skill1 = db.skillDao().getSkillById("skill_sprint")
        if (skill1 == null) {
            val defaultSkills = listOf(
                SkillEntity(
                    id = "skill_sprint",
                    name = "Sprint (疾走)",
                    type = "ACTIVE",
                    mpCost = 5,
                    level = 1,
                    description = "Movement speed and step tracking efficiency increased by 30%.",
                    cooldownSeconds = 15,
                    isUnlocked = true
                ),
                SkillEntity(
                    id = "skill_bloodlust",
                    name = "Bloodlust (殺氣)",
                    type = "ACTIVE",
                    mpCost = 15,
                    level = 1,
                    description = "Intimidates nearby targets, weakening enemy combat power by 20%.",
                    cooldownSeconds = 30,
                    isUnlocked = true
                ),
                SkillEntity(
                    id = "skill_stealth",
                    name = "Stealth (隱身)",
                    type = "ACTIVE",
                    mpCost = 25,
                    level = 1,
                    description = "Conceals presence and aura entirely from ordinary senses.",
                    cooldownSeconds = 60,
                    isUnlocked = false
                ),
                SkillEntity(
                    id = "skill_rulers_authority",
                    name = "Ruler's Authority (支配者の権能)",
                    type = "ACTIVE",
                    mpCost = 35,
                    level = 1,
                    description = "Telekinetic control over physical objects without physical contact.",
                    cooldownSeconds = 45,
                    isUnlocked = false
                ),
                SkillEntity(
                    id = "skill_shadow_extraction",
                    name = "Shadow Extraction: ARISE (起きろ)",
                    type = "ULTIMATE",
                    mpCost = 50,
                    level = 1,
                    description = "Extracts shadows from fallen enemies and recruits them as permanent loyal shadow soldiers.",
                    cooldownSeconds = 120,
                    isUnlocked = false
                )
            )
            db.skillDao().insertSkills(defaultSkills)
        }
    }

    data class WorkoutTargets(
        val pushups: Int,
        val situps: Int,
        val squats: Int,
        val steps: Int
    )

    fun calculateTargetsForRankAndLevel(rank: String, level: Int): WorkoutTargets {
        val base = when (rank) {
            "E-Rank" -> WorkoutTargets(20, 20, 20, 3000)
            "D-Rank" -> WorkoutTargets(35, 35, 35, 5000)
            "C-Rank" -> WorkoutTargets(50, 50, 50, 7000)
            "B-Rank" -> WorkoutTargets(75, 75, 75, 8500)
            "A-Rank" -> WorkoutTargets(90, 90, 90, 9500)
            "S-Rank" -> WorkoutTargets(100, 100, 100, 10000)
            else -> WorkoutTargets(25, 25, 25, 4000)
        }
        // Dynamic Scaling based on Level
        val levelMultiplier = (level - 1) * 3
        val stepMultiplier = (level - 1) * 200
        return WorkoutTargets(
            pushups = minOf(150, base.pushups + levelMultiplier),
            situps = minOf(150, base.situps + levelMultiplier),
            squats = minOf(150, base.squats + levelMultiplier),
            steps = minOf(20000, base.steps + stepMultiplier)
        )
    }

    suspend fun completeAwakeningAssessment(
        hunterName: String,
        pushupScore: Int,    // 0 to 3
        situpScore: Int,     // 0 to 3
        squatScore: Int,     // 0 to 3
        cardioScore: Int,    // 0 to 3
        focusStat: String    // "STR", "AGI", "VIT", "INT", "BALANCED"
    ) {
        val totalFitnessPoints = pushupScore + situpScore + squatScore + cardioScore
        val assignedRank = when {
            totalFitnessPoints <= 3 -> "E-Rank"
            totalFitnessPoints <= 6 -> "D-Rank"
            totalFitnessPoints <= 8 -> "C-Rank"
            totalFitnessPoints <= 10 -> "B-Rank"
            totalFitnessPoints <= 11 -> "A-Rank"
            else -> "S-Rank"
        }

        val rankTitle = when (assignedRank) {
            "E-Rank" -> "E-RANK HUNTER (EVOLVING)"
            "D-Rank" -> "D-RANK HUNTER (ASPIRANT)"
            "C-Rank" -> "C-RANK HUNTER (RAID CAPTAIN)"
            "B-Rank" -> "B-RANK HUNTER (STRIKE LEADER)"
            "A-Rank" -> "A-RANK HUNTER (ELITE WARRIOR)"
            "S-Rank" -> "S-RANK HUNTER (NATIONAL LEVEL)"
            else -> "HUNTER CANDIDATE"
        }

        var str = 10 + (pushupScore * 3)
        var agi = 10 + (cardioScore * 3)
        var vit = 10 + (squatScore * 3)
        var intStat = 10 + (situpScore * 2)
        var per = 10 + ((pushupScore + cardioScore) / 2)

        when (focusStat) {
            "STR" -> str += 5
            "AGI" -> agi += 5
            "VIT" -> vit += 5
            "INT" -> intStat += 5
            else -> {
                str += 1; agi += 1; vit += 1; intStat += 1; per += 1
            }
        }

        val updatedUser = UserEntity(
            id = 1,
            isAwakened = true,
            name = if (hunterName.isNotBlank()) hunterName.trim().uppercase() else "SUNG JIN-WOO",
            title = rankTitle,
            rank = assignedRank,
            level = 1,
            currentExp = 0,
            maxExp = 100,
            currentHp = 100 + (vit * 2),
            maxHp = 100 + (vit * 2),
            currentMp = 40 + (intStat * 2),
            maxMp = 40 + (intStat * 2),
            fatigue = 0,
            maxFatigue = 100,
            strength = str,
            agility = agi,
            vitality = vit,
            intelligence = intStat,
            perception = per,
            unallocatedPoints = 3,
            gold = 1000L,
            alarmHour = 7,
            alarmMinute = 0,
            alarmEnabled = true,
            penaltyWarningEnabled = true
        )
        db.userDao().insertUser(updatedUser)

        // Generate tailored daily quest according to computed rank
        val targets = calculateTargetsForRankAndLevel(assignedRank, 1)
        val newQuest = DailyQuestEntity(
            id = 1,
            questDate = getTodayDateString(),
            questName = "DAILY TRAINING: PREPARATION TO BECOME STRONG",
            pushupsCurrent = 0,
            pushupsTarget = targets.pushups,
            situpsCurrent = 0,
            situpsTarget = targets.situps,
            squatsCurrent = 0,
            squatsTarget = targets.squats,
            stepsCurrent = 0,
            stepsTarget = targets.steps,
            isCompleted = false,
            isRewardClaimed = false,
            deadlineMillis = getMidnightMillis()
        )
        db.questDao().insertOrUpdateDailyQuest(newQuest)
    }

    suspend fun incrementExercise(exerciseType: String, count: Int = 1) {
        when (exerciseType) {
            "PUSHUPS" -> db.questDao().incrementPushups(count)
            "SITUPS" -> db.questDao().incrementSitups(count)
            "SQUATS" -> db.questDao().incrementSquats(count)
        }
        checkQuestCompletionStatus()
    }

    suspend fun setSteps(steps: Int) {
        db.questDao().setSteps(steps)
        checkQuestCompletionStatus()
    }

    suspend fun addSteps(steps: Int) {
        db.questDao().addSteps(steps)
        checkQuestCompletionStatus()
    }

    private suspend fun checkQuestCompletionStatus() {
        val quest = db.questDao().getDailyQuest() ?: return
        val isAllCompleted = quest.pushupsCurrent >= quest.pushupsTarget &&
                quest.situpsCurrent >= quest.situpsTarget &&
                quest.squatsCurrent >= quest.squatsTarget &&
                quest.stepsCurrent >= quest.stepsTarget

        if (isAllCompleted && !quest.isCompleted) {
            db.questDao().updateDailyQuest(quest.copy(isCompleted = true))
        }
    }

    suspend fun claimDailyQuestRewards(): Boolean {
        val quest = db.questDao().getDailyQuest() ?: return false
        if (!quest.isCompleted || quest.isRewardClaimed) return false

        val user = db.userDao().getUser() ?: return false
        val expGained = 150 * user.level
        val goldGained = 2000L * user.level

        var newExp = user.currentExp + expGained
        var newLevel = user.level
        var newMaxExp = user.maxExp
        var newMaxHp = user.maxHp
        var newMaxMp = user.maxMp
        var newPoints = user.unallocatedPoints

        // Level up checks
        while (newExp >= newMaxExp) {
            newExp -= newMaxExp
            newLevel += 1
            newMaxExp = (newMaxExp * 1.35).toInt()
            newMaxHp += 20
            newMaxMp += 10
            newPoints += 3
        }

        // Full recovery on quest completion like anime
        val updatedUser = user.copy(
            level = newLevel,
            currentExp = newExp,
            maxExp = newMaxExp,
            currentHp = newMaxHp,
            maxHp = newMaxHp,
            currentMp = newMaxMp,
            maxMp = newMaxMp,
            fatigue = 0,
            unallocatedPoints = newPoints,
            gold = user.gold + goldGained
        )
        db.userDao().updateUser(updatedUser)
        db.questDao().updateDailyQuest(quest.copy(isRewardClaimed = true))

        // Check if gate 3 or 4 should unlock
        if (newLevel >= 5) {
            db.questDao().getGateById(3)?.let {
                if (!it.isAvailable) db.questDao().updateGate(it.copy(isAvailable = true))
            }
        }
        if (newLevel >= 10) {
            db.questDao().getGateById(4)?.let {
                if (!it.isAvailable) db.questDao().updateGate(it.copy(isAvailable = true))
            }
        }

        return true
    }

    suspend fun allocateStatPoint(stat: String) {
        when (stat) {
            "STR" -> db.userDao().increaseStrength()
            "AGI" -> db.userDao().increaseAgility()
            "VIT" -> db.userDao().increaseVitality()
            "INT" -> db.userDao().increaseIntelligence()
            "PER" -> db.userDao().increasePerception()
        }
    }

    suspend fun useInventoryItem(item: InventoryItemEntity): Boolean {
        if (item.category == "POTION") {
            db.userDao().fullRecovery()
            if (item.quantity <= 1) {
                db.inventoryDao().deleteItem(item.id)
            } else {
                db.inventoryDao().updateItem(item.copy(quantity = item.quantity - 1))
            }
            return true
        } else if (item.category == "WEAPON") {
            db.inventoryDao().updateItem(item.copy(isEquipped = !item.isEquipped))
            return true
        }
        return false
    }

    suspend fun completeGateRaid(gateId: Int): Boolean {
        val gate = db.questDao().getGateById(gateId) ?: return false
        val user = db.userDao().getUser() ?: return false

        val expGained = gate.expReward
        val goldGained = gate.goldReward

        var newExp = user.currentExp + expGained
        var newLevel = user.level
        var newMaxExp = user.maxExp
        var newMaxHp = user.maxHp
        var newMaxMp = user.maxMp
        var newPoints = user.unallocatedPoints

        while (newExp >= newMaxExp) {
            newExp -= newMaxExp
            newLevel += 1
            newMaxExp = (newMaxExp * 1.35).toInt()
            newMaxHp += 20
            newMaxMp += 10
            newPoints += 3
        }

        val updatedUser = user.copy(
            level = newLevel,
            currentExp = newExp,
            maxExp = newMaxExp,
            currentHp = maxOf(10, user.currentHp - 25),
            fatigue = minOf(100, user.fatigue + 20),
            unallocatedPoints = newPoints,
            gold = user.gold + goldGained
        )
        db.userDao().updateUser(updatedUser)
        db.questDao().updateGate(gate.copy(isCleared = true))

        return true
    }

    suspend fun updateAlarmSettings(hour: Int, minute: Int, enabled: Boolean, penaltyWarning: Boolean) {
        val user = db.userDao().getUser() ?: return
        val updated = user.copy(
            alarmHour = hour,
            alarmMinute = minute,
            alarmEnabled = enabled,
            penaltyWarningEnabled = penaltyWarning
        )
        db.userDao().updateUser(updated)
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getMidnightMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}
