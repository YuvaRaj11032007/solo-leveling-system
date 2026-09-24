package com.sololeveling.system.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
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
import androidx.compose.foundation.layout.width
import com.sololeveling.system.ui.components.HpGauge
import com.sololeveling.system.ui.components.HudLevelBadge
import com.sololeveling.system.ui.components.MpGauge
import com.sololeveling.system.ui.components.ShadowHunterAvatar
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun HeaderStatusCard(
    user: UserEntity,
    onOpenAlarmSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Main Card
        BeveledHudCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp) // space for centered badge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top row with Name, Rank & Alarm icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = user.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            color = TextWhite
                        )
                        Text(
                            text = user.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp,
                            color = NeonCyan
                        )
                    }

                    // Notification & Alarm settings icon button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CutCornerShape(6.dp))
                            .background(Color(0xFF22113D))
                            .border(1.dp, GlowingMagenta, CutCornerShape(6.dp))
                            .clickable(onClick = onOpenAlarmSettings),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alarms",
                            tint = if (user.alarmEnabled) GlowingMagenta else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Middle section: Avatar + Vital Gauges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ShadowHunterAvatar(size = 64.dp)

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        HpGauge(current = user.currentHp, max = user.maxHp)
                        MpGauge(current = user.currentMp, max = user.maxMp)
                        FatigueGauge(current = user.fatigue, max = user.maxFatigue)
                    }
                }

                // EXP Bar
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "EXP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Text(
                        text = "${user.currentExp} / ${user.maxExp} PTS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPurpleLight
                    )
                }
            }
        }

        // Centered Header Badge OVER the top border
        HudLevelBadge(
            level = user.level,
            modifier = Modifier.offset(y = 0.dp)
        )
    }
}
