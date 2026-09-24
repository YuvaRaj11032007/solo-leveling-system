package com.sololeveling.system.ui.screens.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
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
import com.sololeveling.system.data.db.entities.InventoryItemEntity
import com.sololeveling.system.ui.components.BeveledHudCard
import com.sololeveling.system.ui.components.NeonActionButton
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.GoldYellow
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextPurpleMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun InventoryScreen(
    items: List<InventoryItemEntity>,
    gold: Long,
    onUseItem: (InventoryItemEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header & Gold HUD Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "INVENTORY",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                color = TextWhite
            )

            Box(
                modifier = Modifier
                    .clip(CutCornerShape(4.dp))
                    .background(Color(0xFF281805))
                    .border(1.dp, GoldYellow, CutCornerShape(4.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$gold G",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldYellow
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items, key = { it.id }) { item ->
                InventoryItemRow(
                    item = item,
                    onAction = { onUseItem(item) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
private fun InventoryItemRow(
    item: InventoryItemEntity,
    onAction: () -> Unit
) {
    val rarityColor = when (item.rarity) {
        "MYTHIC" -> Color(0xFFF43F5E)
        "LEGENDARY" -> GoldYellow
        "EPIC" -> GlowingMagenta
        "RARE" -> NeonCyan
        else -> Color.LightGray
    }

    val iconVector = when (item.category) {
        "POTION" -> Icons.Default.Healing
        "KEY" -> Icons.Default.Key
        "WEAPON" -> Icons.Default.Security
        else -> Icons.Default.AutoAwesome
    }

    BeveledHudCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (item.isEquipped) GlowingMagenta else NeonPurpleDark
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Item Icon Box
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CutCornerShape(6.dp))
                    .background(Color(0xFF140C26))
                    .border(1.2.dp, rarityColor, CutCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = item.category,
                    tint = rarityColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Item Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite
                    )
                    if (item.quantity > 1) {
                        Text(
                            text = "x${item.quantity}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPurpleLight
                        )
                    }
                }

                Text(
                    text = item.statBonus,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonCyan,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Text(
                    text = item.description,
                    fontSize = 10.sp,
                    color = TextPurpleMuted,
                    lineHeight = 14.sp
                )
            }

            // Action Button
            val actionText = when (item.category) {
                "POTION" -> "[ USE ]"
                "WEAPON" -> if (item.isEquipped) "[ EQUIPPED ]" else "[ EQUIP ]"
                else -> "[ VIEW ]"
            }

            NeonActionButton(
                text = actionText,
                onClick = onAction,
                enabled = true,
                padding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                accentColor = if (item.isEquipped) NeonCyan else GlowingMagenta
            )
        }
    }
}
