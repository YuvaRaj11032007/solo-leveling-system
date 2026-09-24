package com.sololeveling.system.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.NeonPurplePrimary

@Composable
fun ShadowHunterAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 68.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "eye_glow")
    val eyeGlowRadius by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eye_glow_radius"
    )

    val shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp, topEnd = 4.dp, bottomStart = 4.dp)

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(Color(0xFF07040E))
            .border(
                1.5.dp,
                Brush.linearGradient(listOf(GlowingMagenta, NeonPurplePrimary, NeonPurpleDark)),
                shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            // 1. Ethereal background aura / smoke
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonPurpleDark.copy(alpha = 0.45f), Color.Transparent),
                    center = Offset(w / 2f, h / 2f),
                    radius = w * 0.6f
                ),
                radius = w * 0.6f
            )

            // 2. Cloaked silhouette mantle & shoulders
            val mantlePath = Path().apply {
                moveTo(w * 0.15f, h * 0.98f)
                lineTo(w * 0.28f, h * 0.72f)
                lineTo(w * 0.40f, h * 0.60f)
                lineTo(w * 0.50f, h * 0.32f) // hood top
                lineTo(w * 0.60f, h * 0.60f)
                lineTo(w * 0.72f, h * 0.72f)
                lineTo(w * 0.85f, h * 0.98f)
                close()
            }
            drawPath(
                path = mantlePath,
                color = Color(0xFF130C24)
            )
            drawPath(
                path = mantlePath,
                color = NeonPurplePrimary.copy(alpha = 0.5f),
                style = Stroke(width = 1.2f)
            )

            // 3. Shadow Head & Hair Strands
            val headPath = Path().apply {
                moveTo(w * 0.38f, h * 0.48f)
                lineTo(w * 0.50f, h * 0.35f)
                lineTo(w * 0.62f, h * 0.48f)
                lineTo(w * 0.56f, h * 0.64f)
                lineTo(w * 0.50f, h * 0.70f)
                lineTo(w * 0.44f, h * 0.64f)
                close()
            }
            drawPath(
                path = headPath,
                color = Color(0xFF080512)
            )

            // 4. Piercing Glowing White/Cyan Eyes
            val leftEyeCenter = Offset(w * 0.45f, h * 0.52f)
            val rightEyeCenter = Offset(w * 0.55f, h * 0.52f)

            // Outer cyan aura around eyes
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonCyan.copy(alpha = 0.8f), Color.Transparent),
                    center = leftEyeCenter,
                    radius = eyeGlowRadius
                ),
                radius = eyeGlowRadius,
                center = leftEyeCenter
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonCyan.copy(alpha = 0.8f), Color.Transparent),
                    center = rightEyeCenter,
                    radius = eyeGlowRadius
                ),
                radius = eyeGlowRadius,
                center = rightEyeCenter
            )

            // Eye slit lines (intense anime glare)
            drawLine(
                color = Color.White,
                start = Offset(w * 0.41f, h * 0.525f),
                end = Offset(w * 0.47f, h * 0.515f),
                strokeWidth = 2.5f
            )
            drawLine(
                color = Color.White,
                start = Offset(w * 0.59f, h * 0.525f),
                end = Offset(w * 0.53f, h * 0.515f),
                strokeWidth = 2.5f
            )

            // Light streak wisps extending from eyes
            drawLine(
                color = NeonCyan.copy(alpha = 0.7f),
                start = Offset(w * 0.41f, h * 0.525f),
                end = Offset(w * 0.34f, h * 0.50f),
                strokeWidth = 1.2f
            )
            drawLine(
                color = NeonCyan.copy(alpha = 0.7f),
                start = Offset(w * 0.59f, h * 0.525f),
                end = Offset(w * 0.66f, h * 0.50f),
                strokeWidth = 1.2f
            )
        }
    }
}
