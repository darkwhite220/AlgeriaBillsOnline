package earth.darkwhite.feature.onboarding.componenets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import earth.darkwhite.feature.onboarding.OnBoardingPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
    pages: List<OnBoardingPage>,
    pagerState: PagerState,
) {
    HorizontalPager(
        state = pagerState
    ) { page ->
        OnBoardingItem(
            page = pages[page],
        )
    }
}