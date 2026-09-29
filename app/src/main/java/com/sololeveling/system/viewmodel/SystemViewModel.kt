package com.sololeveling.system.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sololeveling.system.alarm.AlarmScheduler
import com.sololeveling.system.alarm.NotificationHelper
import com.sololeveling.system.data.db.AppDatabase
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.data.db.entities.InventoryItemEntity
import com.sololeveling.system.data.db.entities.SkillEntity
import com.sololeveling.system.data.db.entities.UserEntity
import com.sololeveling.system.data.gemini.GeminiService
import com.sololeveling.system.data.repository.SystemRepository
import com.sololeveling.system.sensor.StepCounterManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class SystemViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = SystemRepository(db)
    private val stepCounterManager = StepCounterManager(application)

    val user: StateFlow<UserEntity?> = repository.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyQuest: StateFlow<DailyQuestEntity?> = repository.dailyQuestFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val gates: StateFlow<List<GateQuestEntity>> = repository.gatesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inventory: StateFlow<List<InventoryItemEntity>> = repository.inventoryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val skills: StateFlow<List<SkillEntity>> = repository.skillsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Gemini AI States
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                isUser = false,
                text = "[SYSTEM INTELLIGENCE ONLINE]\nGreetings, Operator. I am THE SYSTEM Architect powered by Gemini Core.\n\nAsk for routine optimization, form advice, custom challenge protocols, or daily performance analysis."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isGeminiLoading = MutableStateFlow(false)
    val isGeminiLoading: StateFlow<Boolean> = _isGeminiLoading.asStateFlow()

    private val _geminiDebrief = MutableStateFlow<String?>(null)
    val geminiDebrief: StateFlow<String?> = _geminiDebrief.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDefaultDataIfNeeded()
            setupAlarmsFromStoredUser()
        }

        if (stepCounterManager.isSensorAvailable) {
            stepCounterManager.startTracking { newSteps ->
                viewModelScope.launch {
                    repository.setSteps(newSteps)
                }
            }
        }
    }

    private suspend fun setupAlarmsFromStoredUser() {
        val currentUser = db.userDao().getUser() ?: return
        if (currentUser.alarmEnabled) {
            AlarmScheduler.scheduleDailyQuestAlarm(
                getApplication(),
                currentUser.alarmHour,
                currentUser.alarmMinute
            )
        }
        if (currentUser.penaltyWarningEnabled) {
            AlarmScheduler.schedulePenaltyWarning(getApplication())
        }
    }

    fun completeAwakening(
        hunterName: String,
        pushupScore: Int,
        situpScore: Int,
        squatScore: Int,
        cardioScore: Int,
        combatFocus: String
    ) {
        viewModelScope.launch {
            repository.completeAwakeningAssessment(
                hunterName = hunterName,
                pushupScore = pushupScore,
                situpScore = situpScore,
                squatScore = squatScore,
                cardioScore = cardioScore,
                focusStat = combatFocus
            )
            NotificationHelper.showDailyQuestNotification(getApplication())
        }
    }

    fun incrementExercise(exerciseType: String, count: Int = 1) {
        viewModelScope.launch {
            repository.incrementExercise(exerciseType, count)
        }
    }

    fun addSteps(steps: Int) {
        viewModelScope.launch {
            repository.addSteps(steps)
        }
    }

    fun addWater(ml: Int = 250) {
        viewModelScope.launch {
            repository.addWater(ml)
        }
    }

    fun addDeepWork(minutes: Int = 15) {
        viewModelScope.launch {
            repository.addDeepWork(minutes)
        }
    }

    fun addReading(minutes: Int = 10) {
        viewModelScope.launch {
            repository.addReading(minutes)
        }
    }

    fun claimRewards() {
        viewModelScope.launch {
            val success = repository.claimDailyQuestRewards()
            if (success) {
                user.value?.let { u ->
                    NotificationHelper.showLevelUpNotification(getApplication(), u.level)
                }
                // Automatically generate Gemini daily debrief
                requestDailyDebrief()
            }
        }
    }

    fun allocateStat(stat: String) {
        viewModelScope.launch {
            repository.allocateStatPoint(stat)
        }
    }

    fun useItem(item: InventoryItemEntity) {
        viewModelScope.launch {
            repository.useInventoryItem(item)
        }
    }

    fun completeOperation(operationId: Int) {
        viewModelScope.launch {
            repository.completeChallengeOperation(operationId)
        }
    }

    fun saveAlarmPreferences(hour: Int, minute: Int, enabled: Boolean, penalty: Boolean) {
        viewModelScope.launch {
            repository.updateAlarmSettings(hour, minute, enabled, penalty)
            if (enabled) {
                AlarmScheduler.scheduleDailyQuestAlarm(getApplication(), hour, minute)
            } else {
                AlarmScheduler.cancelAlarms(getApplication())
            }
            if (penalty) {
                AlarmScheduler.schedulePenaltyWarning(getApplication())
            }
        }
    }

    fun activateSkill(skill: SkillEntity) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            if (currentUser.currentMp >= skill.mpCost) {
                val updatedUser = currentUser.copy(
                    currentMp = currentUser.currentMp - skill.mpCost
                )
                db.userDao().updateUser(updatedUser)
            }
        }
    }

    // --- GEMINI INTELLIGENCE ACTIONS ---

    fun sendGeminiMessage(messageText: String) {
        if (messageText.isBlank()) return

        val userMsg = ChatMessage(isUser = true, text = messageText.trim())
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isGeminiLoading.value = true
            val currentUser = user.value
            val currentQuest = dailyQuest.value
            val apiKey = currentUser?.geminiApiKey ?: GeminiService.DEFAULT_API_KEY

            val contextBuilder = StringBuilder()
            if (currentUser != null) {
                contextBuilder.append("Player Name: ${currentUser.name}\n")
                contextBuilder.append("Rank: ${currentUser.rank} (${currentUser.title}) | Level: ${currentUser.level}\n")
                contextBuilder.append("Attributes: STR=${currentUser.strength}, AGI=${currentUser.agility}, VIT=${currentUser.vitality}, INT=${currentUser.intelligence}, PER=${currentUser.perception}\n")
                contextBuilder.append("Fatigue: ${currentUser.fatigue} / ${currentUser.maxFatigue}\n")
            }
            if (currentQuest != null) {
                contextBuilder.append("Today's Progress:\n")
                contextBuilder.append("Push-ups: ${currentQuest.pushupsCurrent}/${currentQuest.pushupsTarget}\n")
                contextBuilder.append("Sit-ups: ${currentQuest.situpsCurrent}/${currentQuest.situpsTarget}\n")
                contextBuilder.append("Squats: ${currentQuest.squatsCurrent}/${currentQuest.squatsTarget}\n")
                contextBuilder.append("Steps: ${currentQuest.stepsCurrent}/${currentQuest.stepsTarget}\n")
                contextBuilder.append("Water: ${currentQuest.waterCurrentMl}/${currentQuest.waterTargetMl}ml\n")
                contextBuilder.append("Deep Work: ${currentQuest.deepWorkCurrentMins}/${currentQuest.deepWorkTargetMins}min\n")
            }

            val history = _chatMessages.value.dropLast(1).map {
                (if (it.isUser) "user" else "model") to it.text
            }

            val result = GeminiService.chatWithSystem(
                userMessage = messageText,
                userContext = contextBuilder.toString(),
                conversationHistory = history,
                apiKey = apiKey
            )

            _isGeminiLoading.value = false
            result.fold(
                onSuccess = { reply ->
                    _chatMessages.value = _chatMessages.value + ChatMessage(isUser = false, text = reply)
                },
                onFailure = { err ->
                    _chatMessages.value = _chatMessages.value + ChatMessage(
                        isUser = false,
                        text = "[SYSTEM CONNECTION INTERRUPTED]\n${err.message ?: "Unable to contact Gemini Intelligence. Verify network connection."}"
                    )
                }
            )
        }
    }

    fun requestDailyDebrief() {
        viewModelScope.launch {
            val u = user.value ?: return@launch
            val q = dailyQuest.value ?: return@launch
            _isGeminiLoading.value = true

            val result = GeminiService.generateDailyDebrief(
                playerName = u.name,
                rank = u.rank,
                level = u.level,
                completedPushups = q.pushupsCurrent,
                targetPushups = q.pushupsTarget,
                completedSitups = q.situpsCurrent,
                targetSitups = q.situpsTarget,
                completedSquats = q.squatsCurrent,
                targetSquats = q.squatsTarget,
                steps = q.stepsCurrent,
                waterMl = q.waterCurrentMl,
                focusMins = q.deepWorkCurrentMins,
                apiKey = u.geminiApiKey
            )

            _isGeminiLoading.value = false
            result.fold(
                onSuccess = { debrief ->
                    _geminiDebrief.value = debrief
                    _chatMessages.value = _chatMessages.value + ChatMessage(isUser = false, text = debrief)
                },
                onFailure = { err ->
                    _geminiDebrief.value = "[DEBRIEF UNAVAILABLE: ${err.localizedMessage}]"
                }
            )
        }
    }

    fun requestCustomProtocol(goal: String) {
        viewModelScope.launch {
            val u = user.value ?: return@launch
            _isGeminiLoading.value = true
            _chatMessages.value = _chatMessages.value + ChatMessage(isUser = true, text = "Generate protocol for: $goal")

            val result = GeminiService.generateCustomProtocol(
                topicOrGoal = goal,
                playerRank = u.rank,
                playerLevel = u.level,
                apiKey = u.geminiApiKey
            )

            _isGeminiLoading.value = false
            result.fold(
                onSuccess = { protocol ->
                    _chatMessages.value = _chatMessages.value + ChatMessage(isUser = false, text = protocol)
                },
                onFailure = { err ->
                    _chatMessages.value = _chatMessages.value + ChatMessage(
                        isUser = false,
                        text = "[PROTOCOL ERROR]: ${err.localizedMessage}"
                    )
                }
            )
        }
    }

    fun updateApiKey(newKey: String) {
        viewModelScope.launch {
            repository.updateApiKey(newKey)
        }
    }

    fun clearDebrief() {
        _geminiDebrief.value = null
    }

    override fun onCleared() {
        super.onCleared()
        stepCounterManager.stopTracking()
    }
}
