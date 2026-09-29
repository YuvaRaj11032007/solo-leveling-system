package com.sololeveling.system.data.repository

import com.sololeveling.system.data.db.AppDatabase
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.data.db.entities.InventoryItemEntity
import com.sololeveling.system.data.db.entities.SkillEntity
import com.sololeveling.system.data.db.entities.UserEntity
import com.sololeveling.system.data.gemini.GeminiService
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
                name = "OPERATOR",
                title = "TIER-E CANDIDATE (DISCIPLINE BASELINE)",
                rank = "E-Rank",
                level = 1,
                currentExp = 0,
                maxExp = 100,
                currentHp = 100,
                maxHp = 100,
                currentMp = 50,
                maxMp = 50,
                fatigue = 0,
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
                penaltyWarningEnabled = true,
                geminiApiKey = GeminiService.DEFAULT_API_KEY,
                streakDays = 1
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
                questName = "DAILY DISCIPLINE PROTOCOL: HUMAN PERFORMANCE OPTIMIZATION",
                pushupsCurrent = 0,
                pushupsTarget = targets.pushups,
                situpsCurrent = 0,
                situpsTarget = targets.situps,
                squatsCurrent = 0,
                squatsTarget = targets.squats,
                stepsCurrent = 0,
                stepsTarget = targets.steps,
                waterCurrentMl = 0,
                waterTargetMl = targets.waterMl,
                deepWorkCurrentMins = 0,
                deepWorkTargetMins = targets.deepWorkMins,
                readingCurrentMins = 0,
                readingTargetMins = targets.readingMins,
                isCompleted = false,
                isRewardClaimed = false,
                deadlineMillis = getMidnightMillis()
            )
            db.questDao().insertOrUpdateDailyQuest(newQuest)
        }

        // Initialize Realistic High-Performance Challenge Operations (Replacing Fantasy Gates)
        val currentGate = db.questDao().getGateById(1)
        if (currentGate == null) {
            val initialOperations = listOf(
                GateQuestEntity(
                    id = 1,
                    gateRank = "Tier-D",
                    title = "OPERATION: 5KM AEROBIC THRESHOLD",
                    description = "Cardiovascular capacity sprint. Maintain steady aerobic cadence to elevate VO2 max and stamina density.",
                    enemyName = "5,000 Continuous Strides",
                    enemyHp = 100,
                    enemyMaxHp = 100,
                    workoutObjective = "5,000 Paces + 30 High Knees",
                    expReward = 350,
                    goldReward = 2500L,
                    itemRewardName = "Carbon-Plated Pace Racers",
                    isCleared = false,
                    isAvailable = true
                ),
                GateQuestEntity(
                    id = 2,
                    gateRank = "Tier-C",
                    title = "OPERATION: 90-MIN DEEP WORK SPRINT",
                    description = "Zero-distraction cognitive sprint. Total smartphone severance and uninterrupted high-leverage execution.",
                    enemyName = "90 Minutes Pure Focus",
                    enemyHp = 250,
                    enemyMaxHp = 250,
                    workoutObjective = "Complete 2x 45-Min Pomodoro Blocks",
                    expReward = 750,
                    goldReward = 6000L,
                    itemRewardName = "Active Acoustic Focus Headphones",
                    isCleared = false,
                    isAvailable = true
                ),
                GateQuestEntity(
                    id = 3,
                    gateRank = "Tier-B",
                    title = "OPERATION: COLD RESILIENCE & BREATHWORK",
                    description = "Autonomic nervous system recalibration. Cold thermogenesis coupled with cyclic physiological sighing.",
                    enemyName = "Vagal Nerve Recalibration",
                    enemyHp = 500,
                    enemyMaxHp = 500,
                    workoutObjective = "3-Min Cold Exposure + 10-Min Box Breathing",
                    expReward = 1500,
                    goldReward = 15000L,
                    itemRewardName = "Bio-Optic Blue Blocking Lens",
                    isCleared = false,
                    isAvailable = false
                ),
                GateQuestEntity(
                    id = 4,
                    gateRank = "Tier-A",
                    title = "OPERATION: CENTURY CALISTHENICS CIRCUIT",
                    description = "Elite neuromuscular conditioning. Total upper and lower body calisthenic volume gauntlet.",
                    enemyName = "100 Strict Pull/Push/Squat Cycle",
                    enemyHp = 1000,
                    enemyMaxHp = 1000,
                    workoutObjective = "100 Push-ups + 100 Squats + 50 Core Leg Lifts",
                    expReward = 5000,
                    goldReward = 50000L,
                    itemRewardName = "Weighted Calisthenics Harness (+10kg)",
                    isCleared = false,
                    isAvailable = false
                )
            )
            db.questDao().insertGates(initialOperations)
        }

        // Initialize Realistic High-Performance Gear & Consumables
        val item1 = db.inventoryDao().getItemById("item_elixir")
        if (item1 == null) {
            val defaultItems = listOf(
                InventoryItemEntity(
                    id = "item_elixir",
                    name = "Electrolyte & Mineral Matrix",
                    category = "RECOVERY",
                    rarity = "ELITE",
                    description = "Optimal bioavailable sodium, potassium, and magnesium salts. Instantly resets physiological fatigue.",
                    statBonus = "+100% Stamina & Full Mental Clarity",
                    quantity = 3,
                    isEquipped = false
                ),
                InventoryItemEntity(
                    id = "item_headphones",
                    name = "Acoustic Noise-Cancelling Pro",
                    category = "FOCUS",
                    rarity = "ELITE",
                    description = "High-fidelity isolation shielding auditory channels from ambient distraction.",
                    statBonus = "+20% Deep Work Focus & +10 INT",
                    quantity = 1,
                    isEquipped = true
                ),
                InventoryItemEntity(
                    id = "item_vest",
                    name = "Weighted Calisthenics Vest (+10kg)",
                    category = "GEAR",
                    rarity = "PRO",
                    description = "Ergonomic iron-sand vest engineered for progressive calisthenic overload.",
                    statBonus = "+15 STR & +10 VIT Progression",
                    quantity = 1,
                    isEquipped = false
                ),
                InventoryItemEntity(
                    id = "item_journal",
                    name = "Stoic Retrospective Journal",
                    category = "TOOL",
                    rarity = "PINNACLE",
                    description = "Handbound habit architecture journal for evening audits, cognitive defusion, and goal alignment.",
                    statBonus = "+10 PER & Dopamine Baseline Reset",
                    quantity = 1,
                    isEquipped = false
                )
            )
            db.inventoryDao().insertItems(defaultItems)
        }

        // Initialize Realistic Habit & Performance Protocols (Skills)
        val skill1 = db.skillDao().getSkillById("skill_flow_state")
        if (skill1 == null) {
            val defaultSkills = listOf(
                SkillEntity(
                    id = "skill_flow_state",
                    name = "Hyperfocus Flow State",
                    type = "ACTIVE",
                    mpCost = 10,
                    level = 1,
                    description = "Enters a high-gamma frequency cognitive trance. Blocks external micro-distractions for 45 minutes.",
                    cooldownSeconds = 15,
                    isUnlocked = true
                ),
                SkillEntity(
                    id = "skill_box_breathing",
                    name = "Physiological Sigh & Vagal Reset",
                    type = "ACTIVE",
                    mpCost = 5,
                    level = 1,
                    description = "Two rapid inhales followed by prolonged exhale. Instantly dumps 25 fatigue points and lowers cortisol.",
                    cooldownSeconds = 30,
                    isUnlocked = true
                ),
                SkillEntity(
                    id = "skill_cold_shock",
                    name = "Cold Thermogenesis Shock",
                    type = "ACTIVE",
                    mpCost = 15,
                    level = 1,
                    description = "3-minute cold immersion triggering sustained norepinephrine and a 250% baseline dopamine elevation.",
                    cooldownSeconds = 60,
                    isUnlocked = false
                ),
                SkillEntity(
                    id = "skill_intermittent_fasting",
                    name = "16:8 Metabolic Autophagy",
                    type = "PASSIVE",
                    mpCost = 0,
                    level = 1,
                    description = "Periodic nutrient withholding prompting mitochondrial repair, ketone production, and sharp mental clarity.",
                    cooldownSeconds = 0,
                    isUnlocked = true
                ),
                SkillEntity(
                    id = "skill_circadian_lock",
                    name = "Circadian Sleep Lock",
                    type = "PROTOCOL",
                    mpCost = 25,
                    level = 1,
                    description = "Strict darkness protocol and temperature reduction. Guarantees 90+ minutes of deep slow-wave REM sleep.",
                    cooldownSeconds = 120,
                    isUnlocked = false
                )
            )
            db.skillDao().insertSkills(defaultSkills)
        }
    }

    data class RealisticTargets(
        val pushups: Int,
        val situps: Int,
        val squats: Int,
        val steps: Int,
        val waterMl: Int,
        val deepWorkMins: Int,
        val readingMins: Int
    )

    fun calculateTargetsForRankAndLevel(rank: String, level: Int): RealisticTargets {
        val base = when (rank) {
            "E-Rank" -> RealisticTargets(20, 20, 20, 4000, 2500, 45, 15)
            "D-Rank" -> RealisticTargets(35, 35, 35, 6000, 2800, 60, 20)
            "C-Rank" -> RealisticTargets(50, 50, 50, 8000, 3000, 75, 25)
            "B-Rank" -> RealisticTargets(70, 70, 70, 10000, 3200, 90, 30)
            "A-Rank" -> RealisticTargets(85, 85, 85, 12000, 3500, 105, 35)
            "S-Rank" -> RealisticTargets(100, 100, 100, 14000, 4000, 120, 45)
            else -> RealisticTargets(25, 25, 25, 5000, 2500, 45, 15)
        }
        val levelMul = (level - 1) * 2
        val stepMul = (level - 1) * 150
        return RealisticTargets(
            pushups = minOf(150, base.pushups + levelMul),
            situps = minOf(150, base.situps + levelMul),
            squats = minOf(150, base.squats + levelMul),
            steps = minOf(20000, base.steps + stepMul),
            waterMl = base.waterMl,
            deepWorkMins = minOf(180, base.deepWorkMins + (level - 1) * 5),
            readingMins = minOf(60, base.readingMins + (level - 1) * 2)
        )
    }

    suspend fun completeAwakeningAssessment(
        hunterName: String,
        pushupScore: Int,
        situpScore: Int,
        squatScore: Int,
        cardioScore: Int,
        focusStat: String
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
            "E-Rank" -> "TIER-E: EVOLVING ASPIRANT"
            "D-Rank" -> "TIER-D: DISCIPLINE PRACTITIONER"
            "C-Rank" -> "TIER-C: HIGH-PERFORMANCE OPERATOR"
            "B-Rank" -> "TIER-B: PROTOCOL LEADER"
            "A-Rank" -> "TIER-A: ELITE BIO-HACKER"
            "S-Rank" -> "TIER-S: PINNACLE HUMAN"
            else -> "DISCIPLINE CANDIDATE"
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
            name = if (hunterName.isNotBlank()) hunterName.trim().uppercase() else "OPERATOR",
            title = rankTitle,
            rank = assignedRank,
            level = 1,
            currentExp = 0,
            maxExp = 100,
            currentHp = 100 + (vit * 2),
            maxHp = 100 + (vit * 2),
            currentMp = 50 + (intStat * 2),
            maxMp = 50 + (intStat * 2),
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
            penaltyWarningEnabled = true,
            geminiApiKey = GeminiService.DEFAULT_API_KEY,
            streakDays = 1
        )
        db.userDao().insertUser(updatedUser)

        val targets = calculateTargetsForRankAndLevel(assignedRank, 1)
        val newQuest = DailyQuestEntity(
            id = 1,
            questDate = getTodayDateString(),
            questName = "DAILY DISCIPLINE PROTOCOL: HUMAN PERFORMANCE OPTIMIZATION",
            pushupsCurrent = 0,
            pushupsTarget = targets.pushups,
            situpsCurrent = 0,
            situpsTarget = targets.situps,
            squatsCurrent = 0,
            squatsTarget = targets.squats,
            stepsCurrent = 0,
            stepsTarget = targets.steps,
            waterCurrentMl = 0,
            waterTargetMl = targets.waterMl,
            deepWorkCurrentMins = 0,
            deepWorkTargetMins = targets.deepWorkMins,
            readingCurrentMins = 0,
            readingTargetMins = targets.readingMins,
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

    suspend fun addWater(ml: Int) {
        db.questDao().addWater(ml)
        checkQuestCompletionStatus()
    }

    suspend fun addDeepWork(minutes: Int) {
        db.questDao().addDeepWork(minutes)
        checkQuestCompletionStatus()
    }

    suspend fun addReading(minutes: Int) {
        db.questDao().addReading(minutes)
        checkQuestCompletionStatus()
    }

    private suspend fun checkQuestCompletionStatus() {
        val quest = db.questDao().getDailyQuest() ?: return
        val isAllCompleted = quest.pushupsCurrent >= quest.pushupsTarget &&
                quest.situpsCurrent >= quest.situpsTarget &&
                quest.squatsCurrent >= quest.squatsTarget &&
                quest.stepsCurrent >= quest.stepsTarget &&
                quest.waterCurrentMl >= quest.waterTargetMl &&
                quest.deepWorkCurrentMins >= quest.deepWorkTargetMins

        if (isAllCompleted && !quest.isCompleted) {
            db.questDao().updateDailyQuest(quest.copy(isCompleted = true))
        }
    }

    suspend fun claimDailyQuestRewards(): Boolean {
        val quest = db.questDao().getDailyQuest() ?: return false
        if (!quest.isCompleted || quest.isRewardClaimed) return false

        val user = db.userDao().getUser() ?: return false
        val expGained = 200 * user.level
        val goldGained = 2500L * user.level

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
            currentHp = newMaxHp,
            maxHp = newMaxHp,
            currentMp = newMaxMp,
            maxMp = newMaxMp,
            fatigue = 0,
            unallocatedPoints = newPoints,
            gold = user.gold + goldGained,
            streakDays = user.streakDays + 1
        )
        db.userDao().updateUser(updatedUser)
        db.questDao().updateDailyQuest(quest.copy(isRewardClaimed = true))

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
        if (item.category == "RECOVERY") {
            db.userDao().fullRecovery()
            if (item.quantity <= 1) {
                db.inventoryDao().deleteItem(item.id)
            } else {
                db.inventoryDao().updateItem(item.copy(quantity = item.quantity - 1))
            }
            return true
        } else if (item.category == "GEAR" || item.category == "FOCUS") {
            db.inventoryDao().updateItem(item.copy(isEquipped = !item.isEquipped))
            return true
        }
        return false
    }

    suspend fun completeChallengeOperation(operationId: Int): Boolean {
        val op = db.questDao().getGateById(operationId) ?: return false
        val user = db.userDao().getUser() ?: return false

        val expGained = op.expReward
        val goldGained = op.goldReward

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
            unallocatedPoints = newPoints,
            gold = user.gold + goldGained
        )
        db.userDao().updateUser(updatedUser)
        db.questDao().updateGate(op.copy(isCleared = true))

        return true
    }

    suspend fun updateApiKey(newKey: String) {
        val user = db.userDao().getUser() ?: return
        db.userDao().updateUser(user.copy(geminiApiKey = newKey))
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
