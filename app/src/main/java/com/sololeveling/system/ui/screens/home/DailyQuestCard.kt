package com.sololeveling.system.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.data.db.entities.DailyQuestEntity
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.components.QuestProgressBar
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GoldYellow
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun DailyQuestCard(
    quest: DailyQuestEntity,
    onIncrementExercise: (String, Int) -> Unit,
    onSimulateSteps: (Int) -> Unit,
    onAddWater: (Int) -> Unit,
    onAddDeepWork: (Int) -> Unit,
    onAddReading: (Int) -> Unit,
    onClaimRewards: () -> Unit,
    modifier: Modifier = Modifier
) {
    var millisRemaining by remember { mutableLongStateOf(calculateMillisToMidnight()) }

    // Midnight Countdown Ticker
    LaunchedEffect(Unit) {
        while (true) {
            millisRemaining = calculateMillisToMidnight()
            delay(1000)
        }
    }

    // Active Live Focus Timer State
    var isFocusTimerRunning by remember { mutableStateOf(false) }
    var focusTimerSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isFocusTimerRunning) {
        while (isFocusTimerRunning) {
            delay(1000)
            focusTimerSeconds++
            if (focusTimerSeconds >= 60) {
                onAddDeepWork(1)
                focusTimerSeconds = 0
            }
        }
    }

    val hours = (millisRemaining / (1000 * 60 * 60)) % 24
    val minutes = (millisRemaining / (1000 * 60)) % 60
    val seconds = (millisRemaining / 1000) % 60
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    val allObjectivesMet = quest.pushupsCurrent >= quest.pushupsTarget &&
            quest.situpsCurrent >= quest.situpsTarget &&
            quest.squatsCurrent >= quest.squatsTarget &&
            quest.stepsCurrent >= quest.stepsTarget &&
            quest.waterCurrentMl >= quest.waterTargetMl &&
            quest.deepWorkCurrentMins >= quest.deepWorkTargetMins

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "DAILY DISCIPLINE PROTOCOLS",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.2.sp,
            color = TextWhite,
            modifier = Modifier.padding(bottom = 6.dp)
        )

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
                        text = "[PROTOCOL ARRIVED: PERFORMANCE BASELINE]",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                        color = GlowingMagenta
                    )
                    Text(
                        text = if (quest.isRewardClaimed) "COMPLETED" else if (allObjectivesMet) "READY TO CLAIM" else "ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (allObjectivesMet) NeonCyan else TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // SECTION 1: PHYSICAL CALISTHENICS & MOVEMENT
                SectionSubHeader("1. PHYSICAL CALISTHENICS & MOVEMENT")

                QuestProgressBar(
                    title = "Push-ups (Chest / Triceps)",
                    current = quest.pushupsCurrent,
                    target = quest.pushupsTarget,
                    onIncrementClick = { onIncrementExercise("PUSHUPS", 5) }
                )

                QuestProgressBar(
                    title = "Sit-ups / Core Stability",
                    current = quest.situpsCurrent,
                    target = quest.situpsTarget,
                    onIncrementClick = { onIncrementExercise("SITUPS", 5) }
                )

                QuestProgressBar(
                    title = "Bodyweight Squats (Lower Body)",
                    current = quest.squatsCurrent,
                    target = quest.squatsTarget,
                    onIncrementClick = { onIncrementExercise("SQUATS", 5) }
                )

                // Steps with Sensor + Quick Log
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        QuestProgressBar(
                            title = "Daily Aerobic Paces (Sensor)",
                            current = quest.stepsCurrent,
                            target = quest.stepsTarget,
                            unit = "steps"
                        )
                    }

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

                // SECTION 2: HYDRATION & RECOVERY
                SectionSubHeader("2. HYDRATION & COGNITIVE OPTIMIZATION")

                // Hydration Progress Row
                RealisticHabitProgressRow(
                    title = "Optimal Hydration Target",
                    current = quest.waterCurrentMl,
                    target = quest.waterTargetMl,
                    unit = "ml",
                    barColor = NeonCyan,
                    quickActionLabel = "+250ml",
                    icon = Icons.Default.LocalDrink,
                    onAction = { onAddWater(250) }
                )

                // Deep Work Focus Row with Interactive Live Stopwatch
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    RealisticHabitProgressRow(
                        title = "Deep Cognitive Focus (Distraction-Free)",
                        current = quest.deepWorkCurrentMins,
                        target = quest.deepWorkTargetMins,
                        unit = "mins",
                        barColor = GlowingMagenta,
                        quickActionLabel = "+15m",
                        icon = Icons.Default.Timer,
                        onAction = { onAddDeepWork(15) }
                    )

                    // Live Focus Stopwatch Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .clip(CutCornerShape(4.dp))
                            .background(Color(0xFF130922))
                            .border(1.dp, NeonPurpleDark.copy(alpha = 0.5f), CutCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CutCornerShape(2.dp))
                                        .background(if (isFocusTimerRunning) Color(0xFF10B981) else Color.Gray)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isFocusTimerRunning) "LIVE FLOW BLOCK: ${focusTimerSeconds}s" else "LIVE POMODORO TIMER: READY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isFocusTimerRunning) Color(0xFF34D399) else TextMuted
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(CutCornerShape(4.dp))
                                    .background(if (isFocusTimerRunning) Color(0xFF450A0A) else Color(0xFF064E3B))
                                    .border(1.dp, if (isFocusTimerRunning) Color(0xFFEF4444) else Color(0xFF10B981), CutCornerShape(4.dp))
                                    .clickable { isFocusTimerRunning = !isFocusTimerRunning }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (isFocusTimerRunning) "PAUSE SPRINT" else "START SPRINT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                            }
                        }
                    }
                }

                // Reading / Learning Row
                RealisticHabitProgressRow(
                    title = "Skill Mastery & Reading",
                    current = quest.readingCurrentMins,
                    target = quest.readingTargetMins,
                    unit = "mins",
                    barColor = NeonPurpleLight,
                    quickActionLabel = "+10m",
                    icon = Icons.Default.Book,
                    onAction = { onAddReading(10) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom row: Countdown timer + [ CLAIM DISCIPLINE REWARDS ] button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "RESET COUNTDOWN:",
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
                        text = if (quest.isRewardClaimed) "[ PROTOCOL COMPLETED ]" else "[ CLAIM REWARDS ]",
                        onClick = onClaimRewards,
                        enabled = allObjectivesMet && !quest.isRewardClaimed,
                        accentColor = if (allObjectivesMet) GlowingMagenta else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionSubHeader(title: String) {
    Text(
        text = title,
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 0.8.sp,
        color = NeonCyan,
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
    )
}

@Composable
private fun RealisticHabitProgressRow(
    title: String,
    current: Int,
    target: Int,
    unit: String,
    barColor: Color,
    quickActionLabel: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onAction: () -> Unit
) {
    val progress = if (target > 0) (current.toFloat() / target).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "habit_progress"
    )

    val shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)
    val isComplete = current >= target

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = barColor,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = title.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isComplete) barColor else TextWhite
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "$current / $target $unit",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isComplete) NeonPurpleLight else TextMuted
                )

                if (!isComplete) {
                    Box(
                        modifier = Modifier
                            .clip(CutCornerShape(4.dp))
                            .background(Color(0xFF241544))
                            .border(1.dp, barColor.copy(alpha = 0.8f), CutCornerShape(4.dp))
                            .clickable(onClick = onAction)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = quickActionLabel,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .height(13.dp)
                .clip(shape)
                .background(Color(0xFF0D091B))
                .border(
                    width = 1.dp,
                    color = if (isComplete) barColor.copy(alpha = 0.8f) else NeonPurpleDark.copy(alpha = 0.5f),
                    shape = shape
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(shape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(NeonPurpleDark, barColor)
                        )
                    )
            )

            val percent = (progress * 100).toInt()
            Text(
                text = if (isComplete) "COMPLETE [100%]" else "$percent%",
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                modifier = Modifier.align(Alignment.Center)
            )
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
