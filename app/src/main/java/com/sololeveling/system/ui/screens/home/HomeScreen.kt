package com.sololeveling.system.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.data.db.entities.UserEntity
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun HomeScreen(
    user: UserEntity?,
    dailyQuest: DailyQuestEntity?,
    gates: List<GateQuestEntity>,
    onIncrementExercise: (String, Int) -> Unit,
    onSimulateSteps: (Int) -> Unit,
    onAddWater: (Int) -> Unit,
    onAddDeepWork: (Int) -> Unit,
    onAddReading: (Int) -> Unit,
    onClaimRewards: () -> Unit,
    onBeginGate: (GateQuestEntity) -> Unit,
    onOpenAlarmSettings: () -> Unit,
    onNavigateToGemini: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (user == null || dailyQuest == null) return

    val activeOperation = gates.firstOrNull { !it.isCleared && it.isAvailable } ?: gates.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Header / Biological & Performance Status
        HeaderStatusCard(
            user = user,
            onOpenAlarmSettings = onOpenAlarmSettings
        )

        // 2. Gemini System Intelligence Callout Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CutCornerShape(6.dp))
                .background(Color(0xFF130A24))
                .border(1.dp, NeonCyan.copy(alpha = 0.6f), CutCornerShape(6.dp))
                .clickable(onClick = onNavigateToGemini)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CutCornerShape(4.dp))
                        .background(Color(0xFF0F1E33))
                        .border(1.dp, NeonCyan, CutCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Gemini",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "THE SYSTEM ARCHITECT // GEMINI ONLINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                        color = NeonCyan
                    )
                    Text(
                        text = "Tap to consult AI on form, custom protocols & daily debrief.",
                        fontSize = 10.sp,
                        color = TextPurpleMuted
                    )
                }

                Text(
                    text = "OPEN ➔",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = GlowingMagenta
                )
            }
        }

        // 3. Daily Discipline Protocols (Physical + Hydration + Deep Work + Reading)
        DailyQuestCard(
            quest = dailyQuest,
            onIncrementExercise = onIncrementExercise,
            onSimulateSteps = onSimulateSteps,
            onAddWater = onAddWater,
            onAddDeepWork = onAddDeepWork,
            onAddReading = onAddReading,
            onClaimRewards = onClaimRewards
        )

        // 4. Real-World Milestone Challenge Operation
        GateClearanceCard(
            gate = activeOperation,
            onBeginGate = onBeginGate
        )

        Spacer(modifier = Modifier.height(72.dp))
    }
}
