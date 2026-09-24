package com.sololeveling.system.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.data.db.entities.UserEntity

@Composable
fun HomeScreen(
    user: UserEntity?,
    dailyQuest: DailyQuestEntity?,
    gates: List<GateQuestEntity>,
    onIncrementExercise: (String, Int) -> Unit,
    onSimulateSteps: (Int) -> Unit,
    onClaimRewards: () -> Unit,
    onBeginGate: (GateQuestEntity) -> Unit,
    onOpenAlarmSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (user == null || dailyQuest == null) return

    val activeGate = gates.firstOrNull { !it.isCleared && it.isAvailable } ?: gates.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Header / Status Card
        HeaderStatusCard(
            user = user,
            onOpenAlarmSettings = onOpenAlarmSettings
        )

        // 2. Daily Quests Container
        DailyQuestCard(
            quest = dailyQuest,
            onIncrementExercise = onIncrementExercise,
            onSimulateSteps = onSimulateSteps,
            onClaimRewards = onClaimRewards
        )

        // 3. Sub-Quest / Gate Clearance Card
        GateClearanceCard(
            gate = activeGate,
            onBeginGate = onBeginGate
        )

        Spacer(modifier = Modifier.height(70.dp)) // space for floating bottom dock
    }
}
