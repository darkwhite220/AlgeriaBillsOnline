package earth.darkwhite.feature.onboarding

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import earth.core.designsystem.icon.AppIcons
import earth.darkwhite.feature.onboarding.componenets.BottomContent
import earth.darkwhite.feature.onboarding.componenets.LanguageDialog
import earth.darkwhite.feature.onboarding.componenets.MainContent
import earth.darkwhite.feature.onboarding.componenets.OnBoardingTopBar
import earth.feature.feature.onboarding.R
import kotlinx.coroutines.launch


@Composable
internal fun OnBoardingRoute(
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    OnBoardingScreen(
        onStartClick = viewModel::onStartClick,
        onLanguageClick = { showLanguageDialog = true }
    )
    
    if (showLanguageDialog) {
        LanguageDialog(
            onDismissClick = { showLanguageDialog = false },
            onLanguageClick = { languageConfig ->
                viewModel.onLanguageChange(languageConfig)
                showLanguageDialog = false
            },
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun OnBoardingScreen(
    pages: List<OnBoardingPage> = onBoardingPages,
    onStartClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val showBackButton by remember { derivedStateOf { pagerState.currentPage > 0 } }
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = { OnBoardingTopBar(onLanguageClick = onLanguageClick) },
        bottomBar = {
            BottomContent(
                size = pages.size,
                index = pagerState.currentPage,
                showBackButton = showBackButton,
                onBackClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                onNextClicked = {
                    if (pagerState.currentPage < pages.lastIndex) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onStartClick()
                    }
                },
            )
        }
    ) {
        MainContent(
            pages = pages,
            pagerState = pagerState,
        )
    }
}

@Immutable
data class OnBoardingPage(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
)

val onBoardingPages = listOf(
    OnBoardingPage(
        imageRes = AppIcons.OnBoardingPage1,
        titleRes = R.string.page_one_title,
        descriptionRes = R.string.page_one_title_desc,
    ),
    OnBoardingPage(
        imageRes = AppIcons.OnBoardingPage2,
        titleRes = R.string.page_two_title,
        descriptionRes = R.string.page_two_title_desc,
    ),
    OnBoardingPage(
        imageRes = AppIcons.OnBoardingPage3,
        titleRes = R.string.page_three_title,
        descriptionRes = R.string.page_three_title_desc,
    ),
)