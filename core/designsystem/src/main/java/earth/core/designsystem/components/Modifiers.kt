package earth.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val smallDp = 4.dp
val mediumDp = 8.dp
val LargeDp = 16.dp

@Composable
fun horizontalSpacedBy(value: Dp = mediumDp): Arrangement.Horizontal = Arrangement.spacedBy(value)

@Composable
fun verticalSpacedBy(value: Dp = mediumDp): Arrangement.Vertical = Arrangement.spacedBy(value)