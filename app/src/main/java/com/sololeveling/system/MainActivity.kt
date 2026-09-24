package com.sololeveling.system

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.sololeveling.system.alarm.NotificationHelper
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.ui.components.HudBackground
import com.sololeveling.system.ui.navigation.NavigationItem
import com.sololeveling.system.ui.navigation.SystemBottomNavBar
import com.sololeveling.system.ui.screens.dungeon.GateBattleDialog
import com.sololeveling.system.ui.screens.home.HomeScreen
import com.sololeveling.system.ui.screens.inventory.InventoryScreen
import com.sololeveling.system.ui.screens.onboarding.AwakeningAssessmentScreen
import com.sololeveling.system.ui.screens.settings.AlarmSettingsDialog
import com.sololeveling.system.ui.screens.skills.SkillsScreen
import com.sololeveling.system.ui.screens.status.StatusScreen
import com.sololeveling.system.ui.theme.SoloLevelingTheme
import com.sololeveling.system.viewmodel.SystemViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SystemViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Permissions handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()

        setContent {
            SoloLevelingTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }

        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }
}

@Composable
fun MainAppContent(viewModel: SystemViewModel) {
    val user by viewModel.user.collectAsState()
    val dailyQuest by viewModel.dailyQuest.collectAsState()
    val gates by viewModel.gates.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val skills by viewModel.skills.collectAsState()

    var currentRoute by remember { mutableStateOf(NavigationItem.HOME.route) }

    // Dialog States
    var selectedGateForRaid by remember { mutableStateOf<GateQuestEntity?>(null) }
    var showAlarmSettings by remember { mutableStateOf(false) }

    // 1. Initial Assessment Check
    if (user != null && !user!!.isAwakened) {
        AwakeningAssessmentScreen(
            onCompleteAssessment = { name, pushup, situp, squat, cardio, focus ->
                viewModel.completeAwakening(name, pushup, situp, squat, cardio, focus)
            }
        )
        return
    }

    // 2. Main HUD System
    HudBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                SystemBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { currentRoute = it }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentRoute) {
                    NavigationItem.HOME.route -> {
                        HomeScreen(
                            user = user,
                            dailyQuest = dailyQuest,
                            gates = gates,
                            onIncrementExercise = { type, count ->
                                viewModel.incrementExercise(type, count)
                            },
                            onSimulateSteps = { steps ->
                                viewModel.addSteps(steps)
                            },
                            onClaimRewards = {
                                viewModel.claimRewards()
                            },
                            onBeginGate = { gate ->
                                selectedGateForRaid = gate
                            },
                            onOpenAlarmSettings = {
                                showAlarmSettings = true
                            }
                        )
                    }

                    NavigationItem.STATUS.route -> {
                        StatusScreen(
                            user = user,
                            onAllocateStat = { stat ->
                                viewModel.allocateStat(stat)
                            }
                        )
                    }

                    NavigationItem.INVENTORY.route -> {
                        InventoryScreen(
                            items = inventory,
                            gold = user?.gold ?: 0L,
                            onUseItem = { item ->
                                viewModel.useItem(item)
                            }
                        )
                    }

                    NavigationItem.SKILLS.route -> {
                        SkillsScreen(
                            skills = skills,
                            currentMp = user?.currentMp ?: 0,
                            onActivateSkill = { skill ->
                                viewModel.activateSkill(skill)
                            }
                        )
                    }
                }
            }
        }

        // Dungeon Raid Modal Dialog
        selectedGateForRaid?.let { gate ->
            GateBattleDialog(
                gate = gate,
                onDismiss = { selectedGateForRaid = null },
                onRaidVictory = { gateId ->
                    viewModel.completeGate(gateId)
                }
            )
        }

        // Alarm Settings Modal Dialog
        if (showAlarmSettings && user != null) {
            val u = user!!
            AlarmSettingsDialog(
                initialHour = u.alarmHour,
                initialMinute = u.alarmMinute,
                initialAlarmEnabled = u.alarmEnabled,
                initialPenaltyEnabled = u.penaltyWarningEnabled,
                onDismiss = { showAlarmSettings = false },
                onSave = { h, m, enabled, penalty ->
                    viewModel.saveAlarmPreferences(h, m, enabled, penalty)
                },
                onTriggerTestDailyNotification = {
                    NotificationHelper.showDailyQuestNotification(viewModel.getApplication())
                },
                onTriggerTestPenaltyNotification = {
                    NotificationHelper.showPenaltyWarningNotification(viewModel.getApplication())
                }
            )
        }
    }
}
