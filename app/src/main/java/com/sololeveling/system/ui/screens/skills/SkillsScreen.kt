package com.sololeveling.system.ui.screens.skills

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
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
import com.sololeveling.system.data.db.entities.SkillEntity
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun SkillsScreen(
    skills: List<SkillEntity>,
    currentMp: Int,
    onActivateSkill: (SkillEntity) -> Unit,
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
            Text(
                text = "SKILL COMMAND HUD",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                color = TextWhite
            )

            Box(
                modifier = Modifier
                    .clip(CutCornerShape(4.dp))
                    .background(Color(0xFF091A33))
                    .border(1.dp, NeonCyan, CutCornerShape(4.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "MP: $currentMp",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(skills, key = { it.id }) { skill ->
                SkillRow(
                    skill = skill,
                    canCast = currentMp >= skill.mpCost && skill.isUnlocked,
                    onActivate = { onActivateSkill(skill) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
private fun SkillRow(
    skill: SkillEntity,
    canCast: Boolean,
    onActivate: () -> Unit
) {
    val typeColor = when (skill.type) {
        "ULTIMATE" -> GlowingMagenta
        "ACTIVE" -> NeonCyan
        else -> NeonPurpleLight
    }

    val iconVector = when {
        skill.id.contains("shadow") -> Icons.Default.Whatshot
        skill.id.contains("stealth") -> Icons.Default.Visibility
        skill.id.contains("bloodlust") -> Icons.Default.Bolt
        else -> Icons.Default.FlashOn
    }

    BeveledHudCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (skill.isUnlocked) NeonPurpleDark else Color(0xFF1E1430)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CutCornerShape(6.dp))
                    .background(Color(0xFF130926))
                    .border(1.dp, typeColor, CutCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = skill.name,
                    tint = typeColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = skill.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite
                    )
                    Text(
                        text = "LV.${skill.level}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlowingMagenta
                    )
                }

                Text(
                    text = "MP COST: ${skill.mpCost}  |  ${skill.type}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Text(
                    text = skill.description,
                    fontSize = 10.sp,
                    color = TextPurpleMuted,
                    lineHeight = 14.sp
                )
            }

            NeonActionButton(
                text = if (!skill.isUnlocked) "[ LOCKED ]" else "[ CAST ]",
                onClick = onActivate,
                enabled = canCast,
                padding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                accentColor = if (skill.isUnlocked) GlowingMagenta else Color.Gray
            )
        }
    }
}
