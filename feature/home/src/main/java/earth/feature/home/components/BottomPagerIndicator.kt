package earth.feature.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import earth.core.designsystem.components.indicatorOffsetForPage
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
            index = pagerState.currentPage,
            pagerState = pagerState,
        )
    }
}

/**
 * Indicators Row layout
 */
@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun Indicators(
    size: Int = 2,
    index: Int = 0,
    pagerState: PagerState = rememberPagerState { 2 },
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(size) { i ->
            Indicator(
                offset = pagerState.indicatorOffsetForPage(i),
            )
        }
    }
}

/**
 * Indicator
 */
@Composable
private fun Indicator(
    offset: Float,
    indicatorHeight: Dp = indicatorWidthUnselected,
) {
    Box(
        modifier = Modifier
            .height(indicatorHeight)
            .width(lerp(indicatorHeight, indicatorWidthSelected, offset))
            .clip(CircleShape)
            .background(
                color = lerp(
                    start = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                    stop = MaterialTheme.colorScheme.primary,
                    fraction = offset
                )
            )
    )
}
