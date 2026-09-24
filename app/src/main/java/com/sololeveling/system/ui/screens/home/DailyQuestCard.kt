package com.sololeveling.system.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.components.QuestProgressBar
import com.sololeveling.system.ui.components.SmallHudAddButton
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextWhite
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun DailyQuestCard(
    quest: DailyQuestEntity,
    onIncrementExercise: (String, Int) -> Unit,
    onSimulateSteps: (Int) -> Unit,
    onClaimRewards: () -> Unit,
    modifier: Modifier = Modifier
) {
    var millisRemaining by remember { mutableLongStateOf(calculateMillisToMidnight()) }

    // Countdown timer ticker
    LaunchedEffect(Unit) {
        while (true) {
            millisRemaining = calculateMillisToMidnight()
            delay(1000)
        }
    }

    val hours = (millisRemaining / (1000 * 60 * 60)) % 24
    val minutes = (millisRemaining / (1000 * 60)) % 60
    val seconds = (millisRemaining / 1000) % 60
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    val allObjectivesMet = quest.pushupsCurrent >= quest.pushupsTarget &&
            quest.situpsCurrent >= quest.situpsTarget &&
            quest.squatsCurrent >= quest.squatsTarget &&
            quest.stepsCurrent >= quest.stepsTarget

    Column(modifier = modifier.fillMaxWidth()) {
        // Main Container Header
        Text(
            text = "DAILY QUESTS",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.2.sp,
            color = TextWhite,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Inner Quest Window
        BeveledHudCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Inner Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "[QUEST ARRIVED: DAILY TRAINING]",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                        color = GlowingMagenta
                    )
                    Text(
                        text = if (quest.isRewardClaimed) "CLAIMED" else if (allObjectivesMet) "READY" else "ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (allObjectivesMet) NeonCyan else TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Exercise Trackers
                QuestProgressBar(
                    title = "Push-ups",
                    current = quest.pushupsCurrent,
                    target = quest.pushupsTarget,
                    onIncrementClick = { onIncrementExercise("PUSHUPS", 5) }
                )

                QuestProgressBar(
                    title = "Sit-ups",
                    current = quest.situpsCurrent,
                    target = quest.situpsTarget,
                    onIncrementClick = { onIncrementExercise("SITUPS", 5) }
                )

                QuestProgressBar(
                    title = "Squats",
                    current = quest.squatsCurrent,
                    target = quest.squatsTarget,
                    onIncrementClick = { onIncrementExercise("SQUATS", 5) }
                )

                // Steps / Running with Auto Sensor & Quick Add
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        QuestProgressBar(
                            title = "Running / Steps (Auto)",
                            current = quest.stepsCurrent,
                            target = quest.stepsTarget,
                            unit = "steps"
                        )
                    }

                    // Manual Step add button for simulation/convenience
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 16.dp)
                            .clip(CutCornerShape(4.dp))
                            .background(Color(0xFF1E1135))
                            .border(1.dp, NeonPurpleDark, CutCornerShape(4.dp))
                            .clickable { onSimulateSteps(250) }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsWalk,
                                contentDescription = "Step",
                                tint = NeonCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "+250",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom row: Countdown timer + [ COMPLETE ] button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "REMAINING TIME:",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = TextMuted
                        )
                        Text(
                            text = formattedTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = if (hours < 2) Color(0xFFEF4444) else TextWhite
                        )
                    }

                    NeonActionButton(
                        text = if (quest.isRewardClaimed) "[ COMPLETED ]" else "[ COMPLETE ]",
                        onClick = onClaimRewards,
                        enabled = allObjectivesMet && !quest.isRewardClaimed,
                        accentColor = if (allObjectivesMet) GlowingMagenta else Color.Gray
                    )
                }
            }
        }
    }
}

private fun calculateMillisToMidnight(): Long {
    val now = Calendar.getInstance()
    val midnight = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return maxOf(0L, midnight.timeInMillis - now.timeInMillis)
}
