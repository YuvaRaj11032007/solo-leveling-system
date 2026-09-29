package com.sololeveling.system.ui.screens.operations

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun OperationsScreen(
    operations: List<GateQuestEntity>,
    onBeginOperation: (GateQuestEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "OPERATIONAL CHALLENGES",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp,
                    color = TextWhite
                )
                Text(
                    text = "Real-World High-Performance Milestones",
                    fontSize = 11.sp,
                    color = TextPurpleMuted
                )
            }

            val clearedCount = operations.count { it.isCleared }
            Box(
                modifier = Modifier
                    .clip(CutCornerShape(4.dp))
                    .background(Color(0xFF1E1038))
                    .border(1.dp, GlowingMagenta, CutCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$clearedCount / ${operations.size} CLEARED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(operations, key = { it.id }) { op ->
                OperationRow(
                    op = op,
                    onBegin = { onBeginOperation(op) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
private fun OperationRow(
    op: GateQuestEntity,
    onBegin: () -> Unit
) {
    val rankColor = when (op.gateRank) {
        "Tier-D" -> GateBlueRank
        "Tier-C", "Tier-B" -> GatePurpleRank
        else -> GateRedRank
    }

    BeveledHudCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (op.isCleared) Color(0xFF1B3B2B) else NeonPurpleDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = op.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Box(
                    modifier = Modifier
                        .clip(CutCornerShape(4.dp))
                        .background(rankColor.copy(alpha = 0.2f))
                        .border(1.dp, rankColor, CutCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = op.gateRank.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }

            Text(
                text = op.description,
                fontSize = 11.sp,
                color = TextPurpleMuted,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )

            // Objectives
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CutCornerShape(4.dp))
                    .background(Color(0xFF0F0A1F))
                    .border(0.8.dp, NeonPurpleDark.copy(alpha = 0.5f), CutCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "REQUIREMENT: ${op.workoutObjective}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Text(
                        text = "TARGET: ${op.enemyName}",
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+${op.expReward} EXP  |  +${op.goldReward} CREDITS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldYellow
                )

                NeonActionButton(
                    text = if (op.isCleared) "[ COMPLETED ]" else if (!op.isAvailable) "[ LOCKED ]" else "[ EXECUTE ]",
                    onClick = onBegin,
                    enabled = !op.isCleared && op.isAvailable,
                    accentColor = if (op.isCleared) Color(0xFF10B981) else if (!op.isAvailable) Color.DarkGray else NeonCyan
                )
            }
        }
    }
}
