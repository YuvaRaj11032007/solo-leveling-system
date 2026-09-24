package com.sololeveling.system.ui.screens.status

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.data.db.entities.UserEntity
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.FatigueGauge
import com.sololeveling.system.ui.components.HpGauge
import com.sololeveling.system.ui.components.MpGauge
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.components.ShadowHunterAvatar
import com.sololeveling.system.ui.components.StatAllocationButton
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GoldYellow
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun StatusScreen(
    user: UserEntity?,
    onAllocateStat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (user == null) return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Window Header
        Text(
            text = "STATUS WINDOW",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.2.sp,
            color = TextWhite
        )

        // Character Profile Summary Card
        BeveledHudCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ShadowHunterAvatar(size = 70.dp)

                    Column {
                        Text(
                            text = user.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Text(
                            text = "JOB: SHADOW MONARCH (EVOLVING)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlowingMagenta
                        )
                        Text(
                            text = "TITLE: ${user.title}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeonCyan
                        )
                        Text(
                            text = "LEVEL: ${user.level}  |  RANK: ${user.rank}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                HpGauge(current = user.currentHp, max = user.maxHp)
                Spacer(modifier = Modifier.height(6.dp))
                MpGauge(current = user.currentMp, max = user.maxMp)
                Spacer(modifier = Modifier.height(6.dp))
                FatigueGauge(current = user.fatigue, max = user.maxFatigue)

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "CURRENT GOLD:", fontSize = 11.sp, color = TextMuted)
                    Text(text = "${user.gold} G", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldYellow)
                }
            }
        }

        // Attributes Allocation HUD Card
        BeveledHudCard(modifier = Modifier.fillMaxWidth()) {
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
                        text = "[ ATTRIBUTES & STATS ]",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                        color = GlowingMagenta
                    )

                    // Unallocated Points Badge
                    Box(
                        modifier = Modifier
                            .clip(CutCornerShape(4.dp))
                            .background(Color(0xFF2B104A))
                            .border(1.dp, GlowingMagenta, CutCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "POINTS: ${user.unallocatedPoints}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (user.unallocatedPoints > 0) NeonCyan else TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val hasPoints = user.unallocatedPoints > 0

                StatRow("STRENGTH (STR)", user.strength, hasPoints) { onAllocateStat("STR") }
                StatRow("AGILITY (AGI)", user.agility, hasPoints) { onAllocateStat("AGI") }
                StatRow("VITALITY (VIT)", user.vitality, hasPoints) { onAllocateStat("VIT") }
                StatRow("INTELLIGENCE (INT)", user.intelligence, hasPoints) { onAllocateStat("INT") }
                StatRow("PERCEPTION (PER)", user.perception, hasPoints) { onAllocateStat("PER") }

                if (user.unallocatedPoints > 0) {
                    Text(
                        text = "※ Distribute available points to increase physical & magical powers.",
                        fontSize = 10.sp,
                        color = NeonCyan,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
private fun StatRow(
    statName: String,
    value: Int,
    canAllocate: Boolean,
    onAllocate: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "$value",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = NeonPurpleLight
            )

            StatAllocationButton(
                onClick = onAllocate,
                enabled = canAllocate
            )
        }
    }
}
