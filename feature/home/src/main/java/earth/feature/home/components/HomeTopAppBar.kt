package earth.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp

@Preview(showBackground = true)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    canScrollBackward: Boolean = false,
    canScrollForward: Boolean = false,
    onBackwardClick: () -> Unit = {},
    onForwardClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = horizontalSpacedBy(largeDp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = largeDp)
            .height(64.dp)
    ) {
        IconButton(
            onClick = onBackwardClick,
            enabled = canScrollBackward
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        
        Image(
            painter = painterResource(id = earth.feature.home.R.drawable.designa),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentDescription = null
        )
        
        this.ProfileItem()
        
        IconButton(
            onClick = onForwardClick,
            enabled = canScrollForward
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
private fun RowScope.ProfileItem() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = "User fullname",
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(alpha = .74f)
        ) {
            Text(
                text = "User Address in full tha is really long",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}