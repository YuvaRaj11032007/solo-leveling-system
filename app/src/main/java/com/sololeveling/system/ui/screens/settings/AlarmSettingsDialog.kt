package com.sololeveling.system.ui.screens.settings

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite
import com.sololeveling.system.ui.theme.VitalityHpRed

@Composable
fun AlarmSettingsDialog(
    initialHour: Int,
    initialMinute: Int,
    initialAlarmEnabled: Boolean,
    initialPenaltyEnabled: Boolean,
    onDismiss: () -> Unit,
    onSave: (hour: Int, minute: Int, enabled: Boolean, penalty: Boolean) -> Unit,
    onTriggerTestDailyNotification: () -> Unit,
    onTriggerTestPenaltyNotification: () -> Unit
) {
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }
    var alarmEnabled by remember { mutableStateOf(initialAlarmEnabled) }
    var penaltyEnabled by remember { mutableStateOf(initialPenaltyEnabled) }

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
                Text(
                    text = "[ SYSTEM NOTIFICATION HUD ]",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = GlowingMagenta
                )

                Text(
                    text = "DAILY ALARMS & PENALTY RADAR",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                // Daily Quest Alarm Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily Quest Arrival Alarm",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Alerts you when daily training arrives",
                            fontSize = 10.sp,
                            color = TextPurpleMuted
                        )
                    }

                    Switch(
                        checked = alarmEnabled,
                        onCheckedChange = { alarmEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GlowingMagenta,
                            checkedTrackColor = Color(0xFF3B1560),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color(0xFF140E20)
                        )
                    )
                }

                // Time adjustment controls
                if (alarmEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CutCornerShape(4.dp))
                            .background(Color(0xFF140A26))
                            .border(1.dp, NeonPurpleDark, CutCornerShape(4.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SCHEDULE TIME:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TimeAdjuster(value = hour, onValueChange = { hour = (it + 24) % 24 })
                                Text(text = ":", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                TimeAdjuster(value = minute, onValueChange = { minute = (it + 60) % 60 })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Penalty Zone Heads-Up Warning Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Penalty Zone Warning (22:00)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Critical heads-up alert 2h before midnight",
                            fontSize = 10.sp,
                            color = TextPurpleMuted
                        )
                    }

                    Switch(
                        checked = penaltyEnabled,
                        onCheckedChange = { penaltyEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = VitalityHpRed,
                            checkedTrackColor = Color(0xFF4A1010),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color(0xFF140E20)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Test notifications buttons
                Text(
                    text = "SYSTEM TEST PROTOCOL:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeonActionButton(
                        text = "TEST QUEST",
                        onClick = onTriggerTestDailyNotification,
                        modifier = Modifier.weight(1f),
                        padding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                        accentColor = NeonCyan
                    )
                    NeonActionButton(
                        text = "TEST PENALTY",
                        onClick = onTriggerTestPenaltyNotification,
                        modifier = Modifier.weight(1f),
                        padding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                        accentColor = VitalityHpRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save / Close
                NeonActionButton(
                    text = "[ SAVE PROTOCOL ]",
                    onClick = {
                        onSave(hour, minute, alarmEnabled, penaltyEnabled)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    accentColor = GlowingMagenta
                )
            }
        }
    }
}

@Composable
private fun TimeAdjuster(value: Int, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CutCornerShape(4.dp))
                .background(Color(0xFF271344))
                .clickable { onValueChange(value - 1) }
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(text = "▼", fontSize = 10.sp, color = TextWhite)
        }

        Text(
            text = String.format("%02d", value),
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )

        Box(
            modifier = Modifier
                .clip(CutCornerShape(4.dp))
                .background(Color(0xFF271344))
                .clickable { onValueChange(value + 1) }
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(text = "▲", fontSize = 10.sp, color = TextWhite)
        }
    }
}
