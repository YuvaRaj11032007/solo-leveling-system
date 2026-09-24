package com.sololeveling.system.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val SystemShapes = Shapes(
    small = CutCornerShape(topStart = 6.dp, bottomEnd = 6.dp),
    medium = CutCornerShape(topStart = 12.dp, topEnd = 4.dp, bottomEnd = 12.dp, bottomStart = 4.dp),
    large = CutCornerShape(topStart = 16.dp, topEnd = 8.dp, bottomEnd = 16.dp, bottomStart = 8.dp)
)

val HudCardShape = CutCornerShape(topStart = 14.dp, topEnd = 6.dp, bottomEnd = 14.dp, bottomStart = 6.dp)
val HudBadgeShape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp)
val HudButtonShape = CutCornerShape(topStart = 10.dp, topEnd = 4.dp, bottomEnd = 10.dp, bottomStart = 4.dp)
