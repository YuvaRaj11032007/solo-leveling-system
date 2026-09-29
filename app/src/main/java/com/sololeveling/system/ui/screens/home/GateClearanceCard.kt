package com.sololeveling.system.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.data.db.entities.GateQuestEntity
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.theme.GateBlueRank
import com.sololeveling.system.ui.theme.GatePurpleRank
import com.sololeveling.system.ui.theme.GateRedRank
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GoldYellow
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun GateClearanceCard(
    gate: GateQuestEntity?,
    onBeginGate: (GateQuestEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (gate == null) return

    val rankColor = when (gate.gateRank) {
        "Tier-D" -> GateBlueRank
        "Tier-C", "Tier-B" -> GatePurpleRank
        else -> GateRedRank
    }

    BeveledHudCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonPurpleDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "[HIGH-PERFORMANCE OPERATION]",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.8.sp,
                    color = GlowingMagenta
                )

                Box(
                    modifier = Modifier
                        .clip(CutCornerShape(4.dp))
                        .background(rankColor.copy(alpha = 0.2f))
                        .border(1.dp, rankColor, CutCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = gate.gateRank.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = gate.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            Text(
                text = gate.description,
                fontSize = 11.sp,
                color = TextPurpleMuted,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CutCornerShape(4.dp))
                    .background(Color(0xFF0F0A1F))
                    .border(0.8.dp, NeonPurpleDark.copy(alpha = 0.5f), CutCornerShape(4.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text(
                            text = "PROTOCOL: ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Text(
                            text = gate.workoutObjective,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }

                    Row {
                        Text(
                            text = "TARGET: ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Text(
                            text = gate.enemyName,
                            fontSize = 10.sp,
                            color = TextPurpleMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "REWARDS:",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Text(
                        text = "+${gate.expReward} EXP  |  +${gate.goldReward} CREDITS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldYellow
                    )
                }

                NeonActionButton(
                    text = if (gate.isCleared) "[ COMPLETED ]" else "[ BEGIN OPERATION ]",
                    onClick = { onBeginGate(gate) },
                    enabled = !gate.isCleared && gate.isAvailable,
                    accentColor = if (gate.isCleared) Color.Gray else NeonCyan
                )
            }
        }
    }
}
