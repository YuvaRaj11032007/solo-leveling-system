package com.sololeveling.system.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.HudBackground
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.components.ShadowHunterAvatar
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GlowingMagentaLight
import com.sololeveling.system.ui.theme.HudBadgeShape
import com.sololeveling.system.ui.theme.HudButtonShape
import com.sololeveling.system.ui.theme.HudCardShape
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.NeonPurplePrimary
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AwakeningAssessmentScreen(
    onCompleteAssessment: (name: String, pushup: Int, situp: Int, squat: Int, cardio: Int, focus: String) -> Unit
) {
    var step by remember { mutableIntStateOf(0) }

    var hunterName by remember { mutableStateOf("SUNG JIN-WOO") }
    var pushupScore by remember { mutableIntStateOf(0) }
    var situpScore by remember { mutableIntStateOf(0) }
    var squatScore by remember { mutableIntStateOf(0) }
    var cardioScore by remember { mutableIntStateOf(0) }
    var combatFocus by remember { mutableStateOf("STR") }

    HudBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut()
                    )
                },
                label = "step_transition"
            ) { currentStep ->
                when (currentStep) {
                    0 -> IntroStep(
                        hunterName = hunterName,
                        onNameChange = { hunterName = it },
                        onNext = { step = 1 }
                    )
                    1 -> PushupEvaluationStep(
                        selected = pushupScore,
                        onSelect = {
                            pushupScore = it
                            step = 2
                        }
                    )
                    2 -> SitupEvaluationStep(
                        selected = situpScore,
                        onSelect = {
                            situpScore = it
                            step = 3
                        }
                    )
                    3 -> SquatEvaluationStep(
                        selected = squatScore,
                        onSelect = {
                            squatScore = it
                            step = 4
                        }
                    )
                    4 -> CardioEvaluationStep(
                        selected = cardioScore,
                        onSelect = {
                            cardioScore = it
                            step = 5
                        }
                    )
                    5 -> CombatFocusStep(
                        selectedFocus = combatFocus,
                        onSelectFocus = { combatFocus = it },
                        onNext = { step = 6 }
                    )
                    6 -> EvaluationResultStep(
                        hunterName = hunterName,
                        pushupScore = pushupScore,
                        situpScore = situpScore,
                        squatScore = squatScore,
                        cardioScore = cardioScore,
                        combatFocus = combatFocus,
                        onInitialize = {
                            onCompleteAssessment(
                                hunterName,
                                pushupScore,
                                situpScore,
                                squatScore,
                                cardioScore,
                                combatFocus
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun IntroStep(
    hunterName: String,
    onNameChange: (String) -> Unit,
    onNext: () -> Unit
) {
    BeveledHudCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShadowHunterAvatar(size = 80.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "[ SYSTEM NOTIFICATION ]",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp,
                color = GlowingMagenta
            )

            Text(
                text = "PLAYER AWAKENING ASSESSMENT",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = TextWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "You have qualified to become a Player of the System. Before granting your Hunter Status, the System must assess your physical capabilities to determine your initial Hunter Rank and tailor your Daily Training Quest.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TextPurpleMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(
                text = "ENTER HUNTER NAME:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = NeonCyan,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = hunterName,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = Color(0xFF140D24),
                    unfocusedContainerColor = Color(0xFF10091C),
                    focusedIndicatorColor = GlowingMagenta,
                    unfocusedIndicatorColor = NeonPurpleDark
                ),
                shape = CutCornerShape(6.dp)
            )

            NeonActionButton(
                text = "BEGIN EVALUATION ➔",
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun QuestionCard(
    questionNumber: String,
    title: String,
    subtitle: String,
    options: List<Pair<String, Int>>,
    selectedScore: Int,
    onSelect: (Int) -> Unit
) {
    BeveledHudCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "[ EVALUATION $questionNumber ]",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GlowingMagenta
            )

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp,
                color = TextWhite,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextPurpleMuted,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            options.forEach { (label, score) ->
                val isSelected = selectedScore == score
                OptionRow(
                    label = label,
                    isSelected = isSelected,
                    onClick = { onSelect(score) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun PushupEvaluationStep(selected: Int, onSelect: (Int) -> Unit) {
    QuestionCard(
        questionNumber = "01 / 05",
        title = "PUSH-UP CAPACITY (UPPER BODY)",
        subtitle = "How many consecutive push-ups can you perform without stopping?",
        options = listOf(
            "Novice (< 10 Push-ups)" to 0,
            "Apprentice (10 - 25 Push-ups)" to 1,
            "Veteran Hunter (26 - 50 Push-ups)" to 2,
            "Shadow Monarch (50+ Push-ups)" to 3
        ),
        selectedScore = selected,
        onSelect = onSelect
    )
}

@Composable
private fun SitupEvaluationStep(selected: Int, onSelect: (Int) -> Unit) {
    QuestionCard(
        questionNumber = "02 / 05",
        title = "SIT-UP CAPACITY (CORE ENDURANCE)",
        subtitle = "How many unbroken sit-ups / crunches can you complete?",
        options = listOf(
            "Novice (< 15 Sit-ups)" to 0,
            "Apprentice (15 - 30 Sit-ups)" to 1,
            "Veteran Hunter (31 - 60 Sit-ups)" to 2,
            "Shadow Monarch (60+ Sit-ups)" to 3
        ),
        selectedScore = selected,
        onSelect = onSelect
    )
}

@Composable
private fun SquatEvaluationStep(selected: Int, onSelect: (Int) -> Unit) {
    QuestionCard(
        questionNumber = "03 / 05",
        title = "SQUAT CAPACITY (LOWER BODY POWER)",
        subtitle = "How many deep bodyweight squats can you perform in one set?",
        options = listOf(
            "Novice (< 20 Squats)" to 0,
            "Apprentice (20 - 40 Squats)" to 1,
            "Veteran Hunter (41 - 70 Squats)" to 2,
            "Shadow Monarch (70+ Squats)" to 3
        ),
        selectedScore = selected,
        onSelect = onSelect
    )
}

@Composable
private fun CardioEvaluationStep(selected: Int, onSelect: (Int) -> Unit) {
    QuestionCard(
        questionNumber = "04 / 05",
        title = "CARDIO & DAILY STEP ENDURANCE",
        subtitle = "What is your typical daily movement and running capacity?",
        options = listOf(
            "Light Movement (< 2,500 steps / Sedentary)" to 0,
            "Moderate Walker (3,000 - 5,000 steps)" to 1,
            "Active Runner (6,000 - 9,000 steps / 5 km)" to 2,
            "Extreme Hunter (10,000+ steps / 10 km daily)" to 3
        ),
        selectedScore = selected,
        onSelect = onSelect
    )
}

@Composable
private fun CombatFocusStep(
    selectedFocus: String,
    onSelectFocus: (String) -> Unit,
    onNext: () -> Unit
) {
    BeveledHudCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "[ EVALUATION 05 / 05 ]",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GlowingMagenta
            )

            Text(
                text = "COMBAT ATTRIBUTE FOCUS",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )

            Text(
                text = "Choose the core attribute that defines your hunter path:",
                fontSize = 12.sp,
                color = TextPurpleMuted,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val options = listOf(
                Triple("STR", "Strength Build", "High physical force, heavy strike power (+5 STR)"),
                Triple("AGI", "Agility / Assassin Build", "Speed, swift reflexes, stealth evasion (+5 AGI)"),
                Triple("VIT", "Vitality / Tank Build", "Immense durability, large HP pool (+5 VIT)"),
                Triple("INT", "Intelligence / Mage Build", "High MP reserve & magical perception (+5 INT)")
            )

            options.forEach { (code, title, desc) ->
                val isSelected = selectedFocus == code
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(6.dp))
                        .background(if (isSelected) Color(0xFF281347) else Color(0xFF130E22))
                        .border(
                            1.dp,
                            if (isSelected) GlowingMagenta else NeonPurpleDark.copy(alpha = 0.5f),
                            CutCornerShape(6.dp)
                        )
                        .clickable { onSelectFocus(code) }
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "[$code]",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) NeonCyan else GlowingMagentaLight
                            )
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                        Text(
                            text = desc,
                            fontSize = 11.sp,
                            color = TextPurpleMuted,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            NeonActionButton(
                text = "CALCULATE HUNTER RANK ➔",
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EvaluationResultStep(
    hunterName: String,
    pushupScore: Int,
    situpScore: Int,
    squatScore: Int,
    cardioScore: Int,
    combatFocus: String,
    onInitialize: () -> Unit
) {
    val totalScore = pushupScore + situpScore + squatScore + cardioScore
    val (rank, rankTitle, initialPushups, initialSitups, initialSquats, initialSteps) = when {
        totalScore <= 3 -> Tuple6("E-Rank", "E-RANK HUNTER (EVOLVING)", 20, 20, 20, 3000)
        totalScore <= 6 -> Tuple6("D-Rank", "D-RANK HUNTER (ASPIRANT)", 35, 35, 35, 5000)
        totalScore <= 8 -> Tuple6("C-Rank", "C-RANK HUNTER (RAID CAPTAIN)", 50, 50, 50, 7000)
        totalScore <= 10 -> Tuple6("B-Rank", "B-RANK HUNTER (STRIKE LEADER)", 75, 75, 75, 8500)
        totalScore <= 11 -> Tuple6("A-Rank", "A-RANK HUNTER (ELITE WARRIOR)", 90, 90, 90, 9500)
        else -> Tuple6("S-Rank", "S-RANK HUNTER (NATIONAL LEVEL)", 100, 100, 100, 10000)
    }

    BeveledHudCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "★ SYSTEM AWAKENING CONFIRMED ★",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                color = GlowingMagenta
            )

            Spacer(modifier = Modifier.height(12.dp))

            ShadowHunterAvatar(size = 72.dp)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = hunterName.uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = TextWhite
            )

            Text(
                text = rankTitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Rank Badge Callout
            Box(
                modifier = Modifier
                    .clip(CutCornerShape(8.dp))
                    .background(Color(0xFF281146))
                    .border(1.5.dp, GlowingMagenta, CutCornerShape(8.dp))
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "ASSIGNED RANK: $rank",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Designed Workout Plan Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CutCornerShape(6.dp))
                    .background(Color(0xFF140D24))
                    .border(1.dp, NeonPurpleDark, CutCornerShape(6.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "CUSTOMIZED DAILY QUEST PLAN:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GlowingMagentaLight,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(text = "• Push-ups: $initialPushups reps / day", fontSize = 12.sp, color = TextWhite)
                    Text(text = "• Sit-ups: $initialSitups reps / day", fontSize = 12.sp, color = TextWhite)
                    Text(text = "• Squats: $initialSquats reps / day", fontSize = 12.sp, color = TextWhite)
                    Text(text = "• Steps / Running: $initialSteps steps / day", fontSize = 12.sp, color = TextWhite)
                    Text(
                        text = "※ Targets will dynamically scale upward as you level up!",
                        fontSize = 10.sp,
                        color = NeonCyan,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            NeonActionButton(
                text = "[ ENTER SYSTEM HUD ]",
                onClick = onInitialize,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CutCornerShape(6.dp))
            .background(if (isSelected) Color(0xFF2E1253) else Color(0xFF130E22))
            .border(
                1.dp,
                if (isSelected) GlowingMagenta else NeonPurpleDark.copy(alpha = 0.4f),
                CutCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (isSelected) TextWhite else TextPurpleMuted
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = GlowingMagenta,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private data class Tuple6<A, B, C, D, E, F>(
    val a: A, val b: B, val c: C, val d: D, val e: E, val f: F
)
