package com.sololeveling.system.ui.screens.operations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GoldYellow
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun OperationExecutionDialog(
    operation: GateQuestEntity,
    onDismiss: () -> Unit,
    onOperationComplete: (Int) -> Unit
) {
    var remainingQuota by remember { mutableIntStateOf(operation.enemyHp) }
    val isCompleted = remainingQuota <= 0

    Dialog(onDismissRequest = onDismiss) {
        BeveledHudCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = GlowingMagenta
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "[ HIGH-PERFORMANCE OPERATION ]",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = GlowingMagenta
                    )
                    Text(
                        text = operation.gateRank,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NeonCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = operation.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Text(
                    text = operation.description,
                    fontSize = 11.sp,
                    color = TextPurpleMuted,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                // Operation Progress Bar
                val progress = if (operation.enemyMaxHp > 0) {
                    ((operation.enemyMaxHp - remainingQuota).toFloat() / operation.enemyMaxHp).coerceIn(0f, 1f)
                } else 1f
                val animatedProgress by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    label = "op_progress"
                )

                val barShape = CutCornerShape(4.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TARGET: ${operation.enemyName.uppercase()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldYellow
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(barShape)
                        .background(Color(0xFF0F0B1E))
                        .border(1.dp, NeonPurpleDark, barShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress)
                            .background(Brush.horizontalGradient(listOf(NeonPurpleDark, NeonCyan, GlowingMagenta)))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tactical Protocol Requirements Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(6.dp))
                        .background(Color(0xFF130922))
                        .border(1.dp, NeonPurpleDark, CutCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "EXECUTION PROTOCOL:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlowingMagenta
                        )
                        Text(
                            text = operation.workoutObjective,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = "Perform this real-world discipline block. Log milestones to complete operation.",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Buttons
                if (!isCompleted) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NeonActionButton(
                            text = "[ LOG SET (-25%) ]",
                            onClick = {
                                remainingQuota = maxOf(0, remainingQuota - (operation.enemyMaxHp / 4).coerceAtLeast(1))
                            },
                            modifier = Modifier.weight(1f),
                            accentColor = NeonCyan
                        )
                        NeonActionButton(
                            text = "[ CLOSE ]",
                            onClick = onDismiss,
                            modifier = Modifier.weight(0.6f),
                            accentColor = Color.Gray
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "★ OPERATION COMPLETE! ★",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldYellow,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Text(
                            text = "AWARDS: +${operation.expReward} EXP  |  +${operation.goldReward} CREDITS\nGEAR UNLOCKED: ${operation.itemRewardName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        NeonActionButton(
                            text = "[ RECORD OPERATIONAL VICTORY ]",
                            onClick = {
                                onOperationComplete(operation.id)
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            accentColor = GlowingMagenta
                        )
                    }
                }
            }
        }
    }
}
