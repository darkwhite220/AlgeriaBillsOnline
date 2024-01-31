package earth.darkwhite.feature.onboarding.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.icon.AppIcons

/**
 * Content: Indicator, Nav Buttons
 */
@Composable
fun BottomContent(
    size: Int,
    index: Int,
    showBackButton: Boolean,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(largeDp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClicked,
            enabled = showBackButton
        ) {
            AnimatedVisibility(
                visible = showBackButton,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Icon(
                    painter = painterResource(AppIcons.ArrowRight),
                    contentDescription = null,
                    modifier = Modifier.rotate(180f)
                )
            }
        }
        
        Indicators(
            size = size,
            index = index,
        )
        
        FilledIconButton(onClick = onNextClicked) {
            Icon(
                painter = painterResource(AppIcons.ArrowRight),
                contentDescription = null
            )
        }
    }
}