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
import com.sololeveling.system.data.repository.SystemRepository
import com.sololeveling.system.sensor.StepCounterManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    init {
        viewModelScope.launch {
            repository.initializeDefaultDataIfNeeded()
            setupAlarmsFromStoredUser()
        }

        // Initialize step sensor listener
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

    fun claimRewards() {
        viewModelScope.launch {
            val success = repository.claimDailyQuestRewards()
            if (success) {
                user.value?.let { u ->
                    NotificationHelper.showLevelUpNotification(getApplication(), u.level)
                }
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

    fun completeGate(gateId: Int) {
        viewModelScope.launch {
            repository.completeGateRaid(gateId)
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

    override fun onCleared() {
        super.onCleared()
        stepCounterManager.stopTracking()
    }
}
