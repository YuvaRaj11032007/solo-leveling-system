package com.sololeveling.system.ui.screens.dungeon

import androidx.compose.animation.core.animateFloatAsState
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
import com.sololeveling.system.ui.theme.VitalityHpRed
import com.sololeveling.system.ui.theme.VitalityHpRedDark

@Composable
fun GateBattleDialog(
    gate: GateQuestEntity,
    onDismiss: () -> Unit,
    onRaidVictory: (Int) -> Unit
) {
    var bossCurrentHp by remember { mutableIntStateOf(gate.enemyHp) }
    val isDefeated = bossCurrentHp <= 0

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
                        text = "[ DUNGEON BOSS RAID ]",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = GlowingMagenta
                    )
                    Text(
                        text = gate.gateRank,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NeonCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "BOSS: ${gate.enemyName.uppercase()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Text(
                    text = gate.description,
                    fontSize = 11.sp,
                    color = TextPurpleMuted,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                // Boss HP Bar
                val progress = if (gate.enemyMaxHp > 0) (bossCurrentHp.toFloat() / gate.enemyMaxHp).coerceIn(0f, 1f) else 0f
                val animatedProgress by animateFloatAsState(targetValue = progress, label = "boss_hp")

                val hpShape = CutCornerShape(4.dp)
                Text(
                    text = "BOSS HP: $bossCurrentHp / ${gate.enemyMaxHp}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = VitalityHpRed
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(hpShape)
                        .background(Color(0xFF220A10))
                        .border(1.dp, VitalityHpRedDark, hpShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress)
                            .background(Brush.horizontalGradient(listOf(VitalityHpRedDark, VitalityHpRed)))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Workout Combat Objective Box
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
                            text = "PHYSICAL TRIAL REQUIREMENT:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlowingMagenta
                        )
                        Text(
                            text = gate.workoutObjective,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = "Execute physical movement reps to strike and deplete boss health!",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                if (!isDefeated) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NeonActionButton(
                            text = "[ STRIKE (-25 HP) ]",
                            onClick = {
                                bossCurrentHp = maxOf(0, bossCurrentHp - 25)
                            },
                            modifier = Modifier.weight(1f),
                            accentColor = VitalityHpRed
                        )
                        NeonActionButton(
                            text = "[ RETREAT ]",
                            onClick = onDismiss,
                            modifier = Modifier.weight(0.7f),
                            accentColor = Color.Gray
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "★ DUNGEON BOSS SLAIN! ★",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldYellow,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Text(
                            text = "REWARDS: +${gate.expReward} EXP  |  +${gate.goldReward} GOLD\nLOOT: ${gate.itemRewardName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        NeonActionButton(
                            text = "[ CLAIM DUNGEON LOOT ]",
                            onClick = {
                                onRaidVictory(gate.id)
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
