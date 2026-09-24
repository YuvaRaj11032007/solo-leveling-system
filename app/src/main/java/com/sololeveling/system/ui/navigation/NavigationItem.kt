package com.sololeveling.system.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Shield
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    HOME("home", "QUESTS", Icons.Default.Diamond),
    STATUS("status", "STATUS", Icons.Default.Shield),
    INVENTORY("inventory", "INVENTORY", Icons.Default.Inventory2),
    SKILLS("skills", "SKILLS", Icons.Default.FlashOn)
}
