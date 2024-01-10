package earth.core.designsystem.components.topappbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.extraLargeDp
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp

@Preview(showBackground = true)
@Composable
fun HomeTopAppBar(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = horizontalSpacedBy(extraLargeDp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = largeDp)
            .height(64.dp)
    ) {
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
//        }
        
        Icon(
            imageVector = Icons.Outlined.Home,
            modifier = Modifier.size(40.dp),
            contentDescription = null
        )
//        Spacer(modifier = Modifier.width(mediumDp))
        this.ProfileItem()

//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
//        }
        
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
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