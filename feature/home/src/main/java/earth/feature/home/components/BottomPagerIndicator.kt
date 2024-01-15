package earth.feature.home.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.indicatorWidthSelected
import earth.core.designsystem.components.indicatorWidthUnselected
import earth.core.designsystem.components.smallDp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomPagerIndicator(pagerState: PagerState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = smallDp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Indicators(
            size = pagerState.pageCount,
            index = pagerState.currentPage
        )
    }
}

/**
 * Indicators Row layout
 */
@Preview(showBackground = true)
@Composable
private fun Indicators(
    size: Int = 2,
    index: Int = 0,
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
private fun Indicator(
    isSelected: Boolean,
    indicatorHeight: Dp = indicatorWidthUnselected
) {
    val width = animateDpAsState(
        targetValue = if (isSelected) indicatorWidthSelected else indicatorHeight,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "width",
    )
    
    Box(
        modifier = Modifier
            .sizeIn(
                minWidth = indicatorHeight,
                minHeight = indicatorHeight,
                maxWidth = indicatorWidthSelected + 6.dp,
                maxHeight = indicatorWidthSelected + 6.dp
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
