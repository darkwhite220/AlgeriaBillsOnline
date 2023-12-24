package earth.darkwhite.algeriabills.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

//enum class TopLevelDestination {
//  HOME,
//  ESTIMATE,
//  SETTINGS,
//}

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
//  val titleTextId: Int,
    val iconTextId: String,
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
//    titleTextId = forYouR.string.for_you,
        iconTextId = "Home",
    ),
    ESTIMATE(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
//    titleTextId = bookmarksR.string.saved,
        iconTextId = "Estimate",
    ),
    SETTINGS(
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
//    titleTextId = interestsR.string.interests,
        iconTextId = "Settings",
    ),
}