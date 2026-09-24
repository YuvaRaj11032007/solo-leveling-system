package com.sololeveling.system.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sololeveling.system.ui.theme.GlowingMagenta
import com.sololeveling.system.ui.theme.HudCardShape
import com.sololeveling.system.ui.theme.NeonPurpleDark
import com.sololeveling.system.ui.theme.NeonPurplePrimary
import com.sololeveling.system.ui.theme.SystemCardBg

@Composable
fun BeveledHudCard(
    modifier: Modifier = Modifier,
    shape: Shape = HudCardShape,
    borderWidth: Dp = 1.5.dp,
    borderColor: Color = GlowingMagenta,
    backgroundColor: Color = SystemCardBg,
    glowColor: Color = NeonPurplePrimary,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                // Subtle outer glow simulation behind the card
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(glowColor.copy(alpha = 0.12f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = maxOf(size.width, size.height) * 0.7f
                    )
                )
            }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.92f)
                    )
                ),
                shape = shape
            )
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GlowingMagenta,
                        NeonPurplePrimary,
                        NeonPurpleDark,
                        GlowingMagenta.copy(alpha = 0.6f)
                    )
                ),
                shape = shape
            )
            .padding(1.dp)
            .border(
                width = 0.8.dp,
                color = NeonPurpleDark.copy(alpha = 0.5f),
                shape = shape
            )
    ) {
        content()
    }
}
