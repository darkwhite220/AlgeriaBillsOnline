package earth.darkwhite.feature.onboarding.componenets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.icon.AppIcons

@Preview(showBackground = true)
@Composable
fun OnBoardingTopBar(
    modifier: Modifier = Modifier,
    onLanguageClick: () -> Unit = {}
) {
    Row {
        Spacer(modifier = modifier.weight(1f))
        IconButton(
            onClick = onLanguageClick,
            modifier = Modifier.padding(largeDp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                painter = painterResource(id = AppIcons.Language),
                contentDescription = null
            )
        }
    }
}