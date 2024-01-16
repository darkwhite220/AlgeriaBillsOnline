package earth.core.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

const val mediumDimAlpha = .74f
val smallDp = 4.dp
val mediumDp = 8.dp
val largeDp = 16.dp
val extraLargeDp = 24.dp
val lottieAnimationSize = 100.dp
val indicatorWidthUnselected = 8.dp
val indicatorWidthSelected = 30.dp
val largeCorner = 16.dp
val homeTopAppBarHeight = 54.dp

@Composable
fun horizontalSpacedBy(value: Dp = mediumDp): Arrangement.Horizontal = Arrangement.spacedBy(value)

@Composable
fun verticalSpacedBy(value: Dp = mediumDp): Arrangement.Vertical = Arrangement.spacedBy(value)

@Composable
fun MyHeightSpacer(height: Dp = mediumDp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun MyWidthSpacer(width: Dp = mediumDp) {
    Spacer(modifier = Modifier.width(width))
}

/**
 * https://www.sinasamaki.com/five-pager-indicator-animations/
 */
@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.indicatorOffsetForPage(page: Int) =
    1f - offsetForPage(page).coerceIn(-1f, 1f).absoluteValue