package earth.darkwhite.algeriabills.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import earth.core.designsystem.icon.AppIcons

enum class TopLevelDestination(
    val selectedIcon: IconRepresentation,
    val unselectedIcon: IconRepresentation,
    val titleTextId: Int,
//    val iconTextId: String,
) {
    HOME(
        selectedIcon = IconRepresentation.Vector(AppIcons.HomeSelected),
        unselectedIcon = IconRepresentation.Vector(AppIcons.HomeUnselected),
        titleTextId = earth.feature.home.R.string.home,
//        iconTextId = "Home",
    ),
    ESTIMATE(
        selectedIcon = IconRepresentation.Drawable(AppIcons.EstimateSelected),
        unselectedIcon = IconRepresentation.Drawable(AppIcons.EstimateUnselected),
        titleTextId = earth.feature.estimate.R.string.estimate,
//        iconTextId = "Estimate",
    ),
    SETTINGS(
        selectedIcon = IconRepresentation.Vector(AppIcons.SettingsSelected),
        unselectedIcon = IconRepresentation.Vector(AppIcons.SettingsUnselected),
        titleTextId = earth.feature.settings.R.string.settings,
//        iconTextId = "Settings",
    ),
}

sealed class IconRepresentation {
    data class Vector(val imageVector: ImageVector) : IconRepresentation()
    data class Drawable(@DrawableRes val drawableId: Int) : IconRepresentation()
}