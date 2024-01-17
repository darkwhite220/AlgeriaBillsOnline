package earth.feature.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import earth.core.database.User
import earth.core.designsystem.components.TextWithEmphasise
import earth.core.designsystem.components.homeTopAppBarHeight
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.components.smallDp
import earth.core.designsystem.icon.AppIcons
import earth.feature.home.UsersListWrapper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeTopAppBar(
    usersListWrapper: UsersListWrapper,
    horizontalState: PagerState,
    verticalState: PagerState,
    modifier: Modifier = Modifier,
    canScrollBackward: Boolean = false,
    canScrollForward: Boolean = false,
    onBackwardClick: () -> Unit = {},
    onForwardClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = horizontalSpacedBy(mediumDp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(homeTopAppBarHeight)
            .padding(horizontal = smallDp)
    ) {
        IconButton(
            onClick = onBackwardClick,
            enabled = canScrollBackward
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        
        VerticalPager(
            state = verticalState,
            key = { item -> item },
            userScrollEnabled = false,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) { index ->
            val users = usersListWrapper.users
            if (index < users.size) {
                ProfileItem(user = users[index])
            }
        }
        
        IconButton(
            onClick = onForwardClick,
            enabled = canScrollForward
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
    
    LaunchedEffect(Unit) {
        snapshotFlow {
            Pair(
                horizontalState.currentPage,
                horizontalState.currentPageOffsetFraction
            )
        }.collect { (page, offset) ->
            verticalState.scrollToPage(page, offset)
        }
    }
}

@Composable
private fun ProfileItem(user: User) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalSpacedBy(mediumDp),
    ) {
        val icon = when {
            user.isHouse && !user.isInState -> AppIcons.HouseOutCity
            !user.isHouse && user.isInState -> AppIcons.BusinessCity
            !user.isHouse && !user.isInState -> AppIcons.BusinessOutCity
            else -> AppIcons.HouseCity
        }
        val shape = RoundedCornerShape(mediumDp)
        Image(
            painter = painterResource(id = icon),
            modifier = Modifier
                .size(44.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = shape)
                .clip(shape),
            contentDescription = null
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            TextWithEmphasise(
                text = user.fullName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                alpha = 1f
            )
            TextWithEmphasise(
                text = user.address,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
            )
        }
    }
}