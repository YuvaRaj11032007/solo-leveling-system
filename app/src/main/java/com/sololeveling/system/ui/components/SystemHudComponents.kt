package com.sololeveling.system.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.HudBadgeShape
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.NeonPurplePrimary
import com.sololeveling.system.ui.theme.SystemObsidian
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextWhite
import kotlin.random.Random

@Composable
fun HudBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aura_shimmer")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_anim"
    )

    // Seeded random embers for background atmosphere
    val particles = remember {
        List(25) {
            Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat() * 2f + 1f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SystemObsidian)
    ) {
        // Fractured ground & subtle aura Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Subtle radial gradient near center and top
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonPurpleDark.copy(alpha = pulse), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.25f),
                    radius = w * 0.9f
                ),
                radius = w * 0.9f,
                center = Offset(w * 0.5f, h * 0.25f)
            )

            // 2. Fractured grid lines in the background
            val gridStep = 80f
            var x = 0f
            while (x <= w) {
                drawLine(
                    color = NeonPurpleDark.copy(alpha = 0.04f),
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1f
                )
                x += gridStep
            }

            var y = 0f
            while (y <= h) {
                drawLine(
                    color = NeonPurpleDark.copy(alpha = 0.04f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f
                )
                y += gridStep
            }

            // Diagonal fracture lines
            val fractureColor = NeonPurpleLight.copy(alpha = 0.05f)
            drawLine(fractureColor, Offset(0f, h * 0.3f), Offset(w * 0.4f, h * 0.5f), 1.2f)
            drawLine(fractureColor, Offset(w * 0.4f, h * 0.5f), Offset(w * 0.35f, h * 0.7f), 1f)
            drawLine(fractureColor, Offset(w * 0.6f, h * 0.1f), Offset(w, h * 0.4f), 1.2f)
            drawLine(fractureColor, Offset(w * 0.2f, h * 0.8f), Offset(w * 0.8f, h), 1f)

            // 3. Ethereal dust embers
            particles.forEach { (px, py, radius) ->
                drawCircle(
                    color = GlowingMagenta.copy(alpha = 0.15f),
                    radius = radius,
                    center = Offset(px * w, ((py + pulse) % 1f) * h)
                )
            }
        }

        content()
    }
}

@Composable
fun HudLevelBadge(
    level: Int,
    modifier: Modifier = Modifier
) {
    val shape = HudBadgeShape

    Box(
        modifier = modifier
            .clip(shape)
            .background(Color(0xFF1E0E35))
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(GlowingMagenta, NeonPurplePrimary, GlowingMagenta)
                ),
                shape
            )
            .padding(horizontal = 14.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LEVEL: ",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = GlowingMagenta
            )
            Text(
                text = "[$level]",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = TextWhite
            )
        }
    }
}

@Composable
fun HudSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "[ $title ]",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.2.sp,
            color = GlowingMagenta
        )
        if (trailingContent != null) {
            Box(modifier = Modifier.padding(start = 8.dp)) {
                trailingContent()
            }
        }
    }
}
