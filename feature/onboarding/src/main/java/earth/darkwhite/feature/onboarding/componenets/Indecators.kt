package earth.darkwhite.feature.onboarding.componenets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * Indicators Row layout
 */
@Composable
fun Indicators(
    size: Int,
    index: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(size) {
            Indicator(
                isSelected = it == index,
            )
        }
    }
}

/**
 * Indicator
 */
@Composable
fun Indicator(
    isSelected: Boolean,
    indicatorHeight: Dp = 8.dp
) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 20.dp else indicatorHeight,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "width",
    )
    
    Box(
        modifier = Modifier
            .sizeIn(
                minWidth = indicatorHeight,
                minHeight = indicatorHeight,
                maxWidth = 26.dp,
                maxHeight = 26.dp
            )
            .height(indicatorHeight)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = .4f)
            )
    )
}