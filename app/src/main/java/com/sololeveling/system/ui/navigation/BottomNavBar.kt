package com.sololeveling.system.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.NeonCyan
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurpleLight
import com.sololeveling.system.ui.theme.NeonPurplePrimary
import com.sololeveling.system.ui.theme.SystemCardBg
import com.sololeveling.system.ui.theme.TextMuted
import com.sololeveling.system.ui.theme.TextWhite

@Composable
fun SystemBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dockShape = CutCornerShape(topStart = 16.dp, topEnd = 16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonPurplePrimary.copy(alpha = 0.2f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = size.width * 0.5f
                    )
                )
            }
            .clip(dockShape)
            .background(Color(0xE6100B22))
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(
                        NeonPurpleDark,
                        GlowingMagenta,
                        NeonPurpleLight,
                        GlowingMagenta,
                        NeonPurpleDark
                    )
                ),
                dockShape
            )
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem.values().forEach { item ->
                val isSelected = currentRoute == item.route

                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) GlowingMagenta else TextMuted,
                    animationSpec = tween(300),
                    label = "icon_tint"
                )

                val textTint by animateColorAsState(
                    targetValue = if (isSelected) TextWhite else TextMuted,
                    animationSpec = tween(300),
                    label = "text_tint"
                )

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onNavigate(item.route) }
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .then(
                                if (isSelected) {
                                    Modifier
                                        .clip(CutCornerShape(6.dp))
                                        .background(NeonPurpleDark.copy(alpha = 0.4f))
                                        .border(1.dp, GlowingMagenta, CutCornerShape(6.dp))
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = textTint,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    // Active glow line under active item
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(width = 18.dp, height = 2.dp)
                                .background(GlowingMagenta, RoundedCornerShape(1.dp))
                        )
                    }
                }
            }
        }
    }
}
