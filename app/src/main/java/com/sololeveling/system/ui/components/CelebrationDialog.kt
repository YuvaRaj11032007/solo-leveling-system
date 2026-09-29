package com.sololeveling.system.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GoldYellow
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextWhite
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun CelebrationDialog(
    newLevel: Int,
    expGained: Int,
    goldGained: Long,
    onDismiss: () -> Unit
) {
    val scaleAnim = remember { Animatable(0.4f) }
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(300))
        scaleAnim.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
    }

    val infiniteTransition = rememberInfiniteTransition(label = "celebration_pulse")
    val shockwaveRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shockwave"
    )

    val shockwaveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shockwave_alpha"
    )

    // Seeded random celebration particles
    val particles = remember {
        List(36) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 120f + 40f
            val color = when (Random.nextInt(4)) {
                0 -> GoldYellow
                1 -> GlowingMagenta
                2 -> NeonCyan
                else -> Color.White
            }
            CelebrationParticle(angle, speed, color, Random.nextFloat() * 3f + 2f)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CutCornerShape(12.dp))
                .background(Color(0xFF0F081C))
                .border(
                    2.dp,
                    Brush.verticalGradient(listOf(GoldYellow, GlowingMagenta, NeonPurpleDark)),
                    CutCornerShape(12.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Canvas for Shockwaves and Particles
            Canvas(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                val center = Offset(size.width / 2f, 70.dp.toPx())

                // Expanding Shockwave ring
                drawCircle(
                    color = GoldYellow.copy(alpha = shockwaveAlpha),
                    radius = shockwaveRadius,
                    center = center,
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )

                // Particle explosion
                particles.forEach { p ->
                    val x = center.x + cos(p.angle) * p.distance * (shockwaveRadius / 180f)
                    val y = center.y + sin(p.angle) * p.distance * (shockwaveRadius / 180f)
                    drawCircle(
                        color = p.color.copy(alpha = shockwaveAlpha),
                        radius = p.size,
                        center = Offset(x, y)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "★ SYSTEM LEVEL UP ★",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = GoldYellow,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "[ DAILY DISCIPLINE REWARDS CLAIMED ]",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = NeonCyan,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Level Badge
                Box(
                    modifier = Modifier
                        .clip(CutCornerShape(8.dp))
                        .background(Color(0xFF2E114E))
                        .border(1.5.dp, GlowingMagenta, CutCornerShape(8.dp))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "NEW LEVEL: $newLevel",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats reward breakdown
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(6.dp))
                        .background(Color(0xFF160B29))
                        .border(1.dp, NeonPurpleDark, CutCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "DISCIPLINE EXP GAINED:", fontSize = 11.sp, color = TextMuted)
                            Text(text = "+$expGained EXP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonPurpleLight)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "MERIT CREDITS GAINED:", fontSize = 11.sp, color = TextMuted)
                            Text(text = "+$goldGained CREDITS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldYellow)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "UNALLOCATED ATTRIBUTE POINTS:", fontSize = 11.sp, color = TextMuted)
                            Text(text = "+3 POINTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "PHYSIOLOGICAL STATUS:", fontSize = 11.sp, color = TextMuted)
                            Text(text = "FULL RECOVERY (0 FATIGUE)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeonActionButton(
                    text = "[ ACCEPT REWARDS ]",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    accentColor = GoldYellow
                )
            }
        }
    }
}

private data class CelebrationParticle(
    val angle: Float,
    val distance: Float,
    val color: Color,
    val size: Float
)
