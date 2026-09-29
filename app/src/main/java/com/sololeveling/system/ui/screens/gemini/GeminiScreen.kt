package com.sololeveling.system.ui.screens.gemini

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GlowingMagentaLight
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.NeonPurplePrimary
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite
import com.sololeveling.system.viewmodel.ChatMessage

@Composable
fun GeminiScreen(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    onSendMessage: (String) -> Unit,
    onRequestDebrief: () -> Unit,
    onRequestCustomProtocol: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto scroll down when messages change
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // 1. Holographic Gemini Core Header
        GeminiCoreHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Tactical Quick-Prompt Chips
        QuickDirectiveChips(
            onRequestDebrief = onRequestDebrief,
            onRequestCustomProtocol = onRequestCustomProtocol,
            onSendPrompt = onSendMessage
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Message Stream
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(messages) { msg ->
                    if (msg.isUser) {
                        PlayerMessageCard(text = msg.text)
                    } else {
                        SystemAiMessageCard(text = msg.text)
                    }
                }

                if (isLoading) {
                    item {
                        SystemAiLoadingCard()
                    }
                }
            }
        }

        // 4. Command Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 72.dp), // Space above dock
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        text = "Transmit directive or question to System...",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = CutCornerShape(6.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = Color(0xFF140D24),
                    unfocusedContainerColor = Color(0xFF0F091C),
                    focusedIndicatorColor = GlowingMagenta,
                    unfocusedIndicatorColor = NeonPurpleDark
                )
            )

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CutCornerShape(6.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF38125C), Color(0xFF1E0B33))
                        )
                    )
                    .border(1.2.dp, GlowingMagenta, CutCornerShape(6.dp))
                    .clickable(enabled = !isLoading && inputText.isNotBlank()) {
                        val toSend = inputText
                        inputText = ""
                        onSendMessage(toSend)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = NeonCyan
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Transmit",
                        tint = if (inputText.isNotBlank()) GlowingMagentaLight else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GeminiCoreHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "gemini_core_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_anim"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_anim"
    )

    BeveledHudCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = NeonPurpleDark
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Animated Holographic Gemini Core
            Box(
                modifier = Modifier.size(46.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = size.width * 0.42f

                    // Outer glowing rotating ring
                    drawCircle(
                        color = NeonPurplePrimary.copy(alpha = 0.2f),
                        radius = radius * pulse
                    )

                    // Arc 1
                    drawArc(
                        brush = Brush.sweepGradient(listOf(NeonCyan, GlowingMagenta, NeonCyan)),
                        startAngle = rotation,
                        sweepAngle = 220f,
                        useCenter = false,
                        style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                    )

                    // Counter-rotating Inner Arc
                    drawArc(
                        brush = Brush.sweepGradient(listOf(GlowingMagenta, NeonPurpleLight, GlowingMagenta)),
                        startAngle = -rotation * 1.5f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = 1.8f, cap = StrokeCap.Round)
                    )

                    // Core center glowing dot
                    drawCircle(
                        color = NeonCyan,
                        radius = 4f * pulse,
                        center = center
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "THE SYSTEM ARCHITECT",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = TextWhite
                    )
                    Box(
                        modifier = Modifier
                            .clip(CutCornerShape(3.dp))
                            .background(Color(0xFF0F3025))
                            .border(0.8.dp, Color(0xFF10B981), CutCornerShape(3.dp))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "GEMINI ACTIVE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF34D399)
                        )
                    }
                }
                Text(
                    text = "Real-World Human Mastery & Performance Intelligence Core",
                    fontSize = 10.sp,
                    color = TextPurpleMuted
                )
            }
        }
    }
}

@Composable
private fun QuickDirectiveChips(
    onRequestDebrief: () -> Unit,
    onRequestCustomProtocol: (String) -> Unit,
    onSendPrompt: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            DirectiveChip(
                label = "📊 DAILY DEBRIEF",
                accentColor = NeonCyan,
                onClick = onRequestDebrief
            )
        }
        item {
            DirectiveChip(
                label = "🎯 90-MIN DEEP WORK",
                accentColor = GlowingMagenta,
                onClick = { onRequestCustomProtocol("90-Minute Zero Distraction Deep Work Sprint") }
            )
        }
        item {
            DirectiveChip(
                label = "🏃 VO2 MAX INTERVALS",
                accentColor = Color(0xFFF59E0B),
                onClick = { onRequestCustomProtocol("VO2 Max High-Intensity Running Intervals") }
            )
        }
        item {
            DirectiveChip(
                label = "⚡ LOW ENERGY RESET",
                accentColor = Color(0xFF38BDF8),
                onClick = { onSendPrompt("I feel fatigued and low dopamine today. Give me an immediate 15-minute physiological reset protocol.") }
            )
        }
        item {
            DirectiveChip(
                label = "🥗 NUTRITION DIRECTIVE",
                accentColor = Color(0xFF10B981),
                onClick = { onSendPrompt("Recommend optimal nutrition, hydration, and meal timing for peak mental alertness and athletic recovery.") }
            )
        }
    }
}

@Composable
private fun DirectiveChip(
    label: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CutCornerShape(4.dp))
            .background(Color(0xFF160E2A))
            .border(1.dp, accentColor.copy(alpha = 0.7f), CutCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
    }
}

@Composable
private fun PlayerMessageCard(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(CutCornerShape(topStart = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                .background(Color(0xFF281146))
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(GlowingMagenta, NeonPurpleLight)),
                    CutCornerShape(topStart = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = "[ PLAYER TRANSMISSION ]",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlowingMagentaLight,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = TextWhite,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun SystemAiMessageCard(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(CutCornerShape(topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp))
                .background(Color(0xFF0F0B1E))
                .border(
                    1.2.dp,
                    Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.8f), NeonPurpleDark)),
                    CutCornerShape(topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp)
                )
                .padding(14.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "System",
                        tint = NeonCyan,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "[ SYSTEM DIRECTIVE // GEMINI ]",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = Color(0xFFF1F1F8),
                    lineHeight = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
private fun SystemAiLoadingCard() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(CutCornerShape(6.dp))
                .background(Color(0xFF120A24))
                .border(1.dp, NeonPurplePrimary, CutCornerShape(6.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = NeonCyan
                )
                Text(
                    text = "THE SYSTEM IS COMPUTING OPTIMAL DIRECTIVE...",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            }
        }
    }
}
