package earth.feature.settings.componenets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.mediumDp


@Composable
fun SettingsDialogOptionChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(mediumDp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalSpacedBy()
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(text)
    }
}
