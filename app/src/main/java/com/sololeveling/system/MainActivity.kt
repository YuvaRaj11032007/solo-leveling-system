package com.sololeveling.system

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.sololeveling.system.alarm.NotificationHelper
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.ui.components.CelebrationDialog
import com.sololeveling.system.ui.components.HudBackground
import com.sololeveling.system.ui.navigation.NavigationItem
import com.sololeveling.system.ui.navigation.SystemBottomNavBar
import com.sololeveling.system.ui.screens.gemini.GeminiScreen
import com.sololeveling.system.ui.screens.home.HomeScreen
import com.sololeveling.system.ui.screens.inventory.InventoryScreen
import com.sololeveling.system.ui.screens.onboarding.AwakeningAssessmentScreen
import com.sololeveling.system.ui.screens.operations.OperationExecutionDialog
import com.sololeveling.system.ui.screens.operations.OperationsScreen
import com.sololeveling.system.ui.screens.settings.AlarmSettingsDialog
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
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isGeminiLoading by viewModel.isGeminiLoading.collectAsState()

    var currentRoute by remember { mutableStateOf(NavigationItem.HOME.route) }

    // Dialog States
    var selectedOperationForExecution by remember { mutableStateOf<GateQuestEntity?>(null) }
    var showAlarmSettings by remember { mutableStateOf(false) }
    var showCelebrationDialog by remember { mutableStateOf(false) }

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
                            onAddWater = { ml ->
                                viewModel.addWater(ml)
                            },
                            onAddDeepWork = { mins ->
                                viewModel.addDeepWork(mins)
                            },
                            onAddReading = { mins ->
                                viewModel.addReading(mins)
                            },
                            onClaimRewards = {
                                viewModel.claimRewards()
                                showCelebrationDialog = true
                            },
                            onBeginGate = { op ->
                                selectedOperationForExecution = op
                            },
                            onOpenAlarmSettings = {
                                showAlarmSettings = true
                            },
                            onNavigateToGemini = {
                                currentRoute = NavigationItem.SYSTEM_AI.route
                            }
                        )
                    }

                    NavigationItem.SYSTEM_AI.route -> {
                        GeminiScreen(
                            messages = chatMessages,
                            isLoading = isGeminiLoading,
                            onSendMessage = { text ->
                                viewModel.sendGeminiMessage(text)
                            },
                            onRequestDebrief = {
                                viewModel.requestDailyDebrief()
                            },
                            onRequestCustomProtocol = { goal ->
                                viewModel.requestCustomProtocol(goal)
                            }
                        )
                    }

                    NavigationItem.OPERATIONS.route -> {
                        OperationsScreen(
                            operations = gates,
                            onBeginOperation = { op ->
                                selectedOperationForExecution = op
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
                }
            }
        }

        // Realistic Operation Execution Modal Dialog
        selectedOperationForExecution?.let { op ->
            OperationExecutionDialog(
                operation = op,
                onDismiss = { selectedOperationForExecution = null },
                onOperationComplete = { opId ->
                    viewModel.completeOperation(opId)
                }
            )
        }

        // Celebration Modal on Claim Rewards / Level Up
        if (showCelebrationDialog && user != null) {
            val u = user!!
            CelebrationDialog(
                newLevel = u.level,
                expGained = 200 * u.level,
                goldGained = 2500L * u.level,
                onDismiss = { showCelebrationDialog = false }
            )
        }

        // Configuration & Alarm Settings Modal Dialog
        if (showAlarmSettings && user != null) {
            val u = user!!
            AlarmSettingsDialog(
                initialHour = u.alarmHour,
                initialMinute = u.alarmMinute,
                initialAlarmEnabled = u.alarmEnabled,
                initialPenaltyEnabled = u.penaltyWarningEnabled,
                currentApiKey = u.geminiApiKey,
                onDismiss = { showAlarmSettings = false },
                onSave = { h, m, enabled, penalty, key ->
                    viewModel.saveAlarmPreferences(h, m, enabled, penalty)
                    viewModel.updateApiKey(key)
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
