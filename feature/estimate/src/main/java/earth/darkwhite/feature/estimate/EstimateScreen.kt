package earth.darkwhite.feature.estimate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.textfield.EditableUserInput
import earth.core.designsystem.components.textfield.UserInput
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.core.designsystem.components.verticalSpacedBy
import earth.feature.estimate.R

@Composable
internal fun EstimateRoute(
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: EstimateViewModel = hiltViewModel()
) {
    EstimateScreen(
        onSettingsClick = onSettingsClick,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
private fun EstimateScreen(
    onSettingsClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val backdropScaffoldState =
        rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
    
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = {
            CenteredTopAppBar(
                titleId = R.string.estimate,
                onBackClick = onBackClick,
            )
        },
        backLayerBackgroundColor = MaterialTheme.colorScheme.background,
        backLayerContent = {
            Column(
                modifier = Modifier
                    .padding(horizontal = largeDp)
                    .padding(bottom = largeDp),
                verticalArrangement = verticalSpacedBy()
            ) {
                UserInput()
                UserInput()
                EditableUserInput()
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = horizontalSpacedBy(largeDp)
//                ) {
//                    Text(
//                        text = "Type of place",
//                        modifier = Modifier.weight(1f)
//                    )
//                    Text(
//                        text = "House",
//                    )
//                    Switch(checked = true, onCheckedChange = {})
//                }
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = horizontalSpacedBy(largeDp)
//                ) {
//                    Text(
//                        text = "Commune capital of daïras",
//                        modifier = Modifier.weight(1f)
//                    )
//                    Text(
//                        text = "Yes",
//                    )
//                    Switch(checked = true, onCheckedChange = {})
//                }
//                TextField(
//                    modifier = Modifier.fillMaxWidth(),
//                    value = "Electricity DPM",
//                    onValueChange = {}
//                )
//                TextField(
//                    modifier = Modifier.fillMaxWidth(),
//                    value = "GAS PCS",
//                    onValueChange = {}
//                )
                
            }
        },
        frontLayerScrimColor = Color.Unspecified,
        frontLayerContent = {
            Surface(tonalElevation = BottomAppBarDefaults.ContainerElevation) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(largeDp),
                    verticalArrangement = verticalSpacedBy()
                ) {
                
                }
            }
        },
    )
}