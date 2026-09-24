package com.sololeveling.system.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.ui.theme.FatigueOrange
import com.sololeveling.system.ui.theme.FatigueOrangeDark
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.ManaMpBlue
import com.sololeveling.system.ui.theme.ManaMpBlueDark
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.NeonPurplePrimary
import com.sololeveling.system.ui.theme.QuestProgressGradientEnd
import com.sololeveling.system.ui.theme.QuestProgressGradientStart
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextWhite
import com.sololeveling.system.ui.theme.VitalityHpRed
import com.sololeveling.system.ui.theme.VitalityHpRedDark

@Composable
fun VitalGaugeRow(
    label: String,
    current: Int,
    max: Int,
    fillBrush: Brush,
    glowColor: Color,
    modifier: Modifier = Modifier,
    height: Dp = 14.dp
) {
    val progress = if (max > 0) (current.toFloat() / max).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "gauge_progress"
    )

    val shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label (e.g. HP, MP, FATIGUE)
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.8.sp,
            color = TextWhite,
            modifier = Modifier.padding(end = 2.dp)
        )

        // Gauge Container
        Box(
            modifier = Modifier
                .weight(1f)
                .height(height)
                .clip(shape)
                .background(Color(0xFF0F0B1A))
                .border(1.dp, NeonPurpleDark.copy(alpha = 0.6f), shape)
        ) {
            // Filled bar
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(shape)
                    .background(fillBrush)
                    .drawBehind {
                        // Glowing leading tip
                        if (size.width > 2) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.7f),
                                start = Offset(size.width - 2, 0f),
                                end = Offset(size.width - 2, size.height),
                                strokeWidth = 2f
                            )
                        }
                    }
            )

            // Value text overlay
            Text(
                text = "$current / $max",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
            )
        }
    }
}

@Composable
fun HpGauge(current: Int, max: Int, modifier: Modifier = Modifier) {
    VitalGaugeRow(
        label = "HP",
        current = current,
        max = max,
        fillBrush = Brush.horizontalGradient(listOf(VitalityHpRedDark, VitalityHpRed)),
        glowColor = VitalityHpRed,
        modifier = modifier
    )
}

@Composable
fun MpGauge(current: Int, max: Int, modifier: Modifier = Modifier) {
    VitalGaugeRow(
        label = "MP",
        current = current,
        max = max,
        fillBrush = Brush.horizontalGradient(listOf(ManaMpBlueDark, ManaMpBlue)),
        glowColor = ManaMpBlue,
        modifier = modifier
    )
}

@Composable
fun FatigueGauge(current: Int, max: Int, modifier: Modifier = Modifier) {
    VitalGaugeRow(
        label = "FATIGUE",
        current = current,
        max = max,
        fillBrush = Brush.horizontalGradient(listOf(FatigueOrangeDark, FatigueOrange)),
        glowColor = FatigueOrange,
        modifier = modifier
    )
}

@Composable
fun QuestProgressBar(
    title: String,
    current: Int,
    target: Int,
    modifier: Modifier = Modifier,
    unit: String = "",
    onIncrementClick: (() -> Unit)? = null
) {
    val progress = if (target > 0) (current.toFloat() / target).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "quest_progress"
    )

    val shape = CutCornerShape(topStart = 6.dp, bottomEnd = 6.dp)
    val isComplete = current >= target

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.6.sp,
                color = if (isComplete) GlowingMagenta else TextWhite
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "$current / $target $unit".trim(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isComplete) NeonPurpleLight else TextMuted
                )

                if (onIncrementClick != null && !isComplete) {
                    SmallHudAddButton(onClick = onIncrementClick)
                }
            }
        }

        // Bar
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .height(16.dp)
                .clip(shape)
                .background(Color(0xFF0D091B))
                .border(
                    width = 1.dp,
                    color = if (isComplete) GlowingMagenta.copy(alpha = 0.8f) else NeonPurpleDark.copy(alpha = 0.6f),
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
                            listOf(
                                QuestProgressGradientStart,
                                QuestProgressGradientEnd,
                                GlowingMagenta
                            )
                        )
                    )
            )

            // Progress percentage in center
            val percent = (progress * 100).toInt()
            Text(
                text = if (isComplete) "COMPLETE [100%]" else "$percent%",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
