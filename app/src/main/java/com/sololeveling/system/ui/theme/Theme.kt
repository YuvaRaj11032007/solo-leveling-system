package com.sololeveling.system.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NeonPurplePrimary,
    secondary = GlowingMagenta,
    tertiary = NeonCyan,
    background = SystemObsidian,
    surface = SystemSurface,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onTertiary = SystemObsidian,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun SoloLevelingTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = SystemObsidian.toArgb()
                window.navigationBarColor = SystemObsidian.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = SystemShapes,
        content = content
    )
}
