package earth.darkwhite.feature.onboarding.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import earth.core.designsystem.components.extraLargeDp
import earth.core.designsystem.components.largeDp
import earth.darkwhite.feature.onboarding.OnBoardingPage


/**
 * ONBOARDING ITEM
 */
@Composable
fun OnBoardingItem(
    page: OnBoardingPage,
    spaceBetweenImageAndTitle: Dp = extraLargeDp,
    spaceBetweenTitleAndDescription: Dp = largeDp,
) {
    Column(
        modifier = Modifier
            .padding(largeDp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
        
        Spacer(modifier = Modifier.height(spaceBetweenImageAndTitle))
        
        Text(
            text = stringResource(page.titleRes),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        
        Spacer(modifier = Modifier.height(spaceBetweenTitleAndDescription))
        
        Text(
            text = stringResource(page.descriptionRes),
            textAlign = TextAlign.Center,
        )
        
        Spacer(modifier = Modifier.fillMaxHeight(.2f))
    }
}