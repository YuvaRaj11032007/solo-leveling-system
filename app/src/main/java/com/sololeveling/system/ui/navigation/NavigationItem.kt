package com.sololeveling.system.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    HOME("home", "DAILY", Icons.Default.Bolt),
    SYSTEM_AI("system_ai", "SYSTEM AI", Icons.Default.AutoAwesome),
    OPERATIONS("operations", "OPS", Icons.Default.Speed),
    STATUS("status", "STATUS", Icons.Default.Shield),
    INVENTORY("inventory", "GEAR", Icons.Default.Inventory2)
}
