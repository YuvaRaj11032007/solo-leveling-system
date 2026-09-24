package com.sololeveling.system.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GlowingMagentaLight
import com.sololeveling.system.ui.theme.HudButtonShape
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.NeonPurplePrimary
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun NeonActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
    accentColor: Color = GlowingMagenta
) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val currentBorderColor = if (enabled) accentColor.copy(alpha = pulseAlpha) else Color.DarkGray
    val currentBgColor = if (enabled) Color(0xFF1E1038) else Color(0xFF14121A)

    Box(
        modifier = modifier
            .drawBehind {
                if (enabled) {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.2f), Color.Transparent),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = maxOf(size.width, size.height) * 0.7f
                        )
                    )
                }
            }
            .clip(HudButtonShape)
            .background(currentBgColor)
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        currentBorderColor,
                        NeonPurplePrimary.copy(alpha = if (enabled) 0.9f else 0.3f),
                        currentBorderColor
                    )
                ),
                shape = HudButtonShape
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp,
            color = if (enabled) TextWhite else Color.Gray
        )
    }
}

@Composable
fun SmallHudAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    label: String = "+"
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(HudButtonShape)
            .background(Color(0xFF241544))
            .border(1.dp, NeonPurplePrimary, HudButtonShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = GlowingMagentaLight
        )
    }
}

@Composable
fun StatAllocationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .size(26.dp)
            .clip(HudButtonShape)
            .background(if (enabled) Color(0xFF381559) else Color(0xFF191424))
            .border(
                1.dp,
                if (enabled) GlowingMagenta else Color.DarkGray,
                HudButtonShape
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = if (enabled) TextWhite else Color.Gray
        )
    }
}
