package earth.core.designsystem.components.topappbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import earth.core.designsystem.icon.AppIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleId: Int? = null,
    @DrawableRes actionIconId: Int? = null,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(painter = painterResource(AppIcons.BackArrow), contentDescription = null)
            }
        },
        title = {
            titleId?.let { Text(text = stringResource(id = it)) }
        },
        actions = {
            actionIconId?.let {
                IconButton(onClick = onActionClick) {
                    Icon(painter = painterResource(actionIconId), contentDescription = null)
                }
            }
        }
    )
}