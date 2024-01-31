package earth.darkwhite.feature.onboarding.componenets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import earth.core.designsystem.icon.AppIcons
import earth.core.preferencesmodel.LanguageConfig
import earth.feature.feature.onboarding.R


@Composable
fun LanguageDialog(
    onDismissClick: () -> Unit = {},
    onLanguageClick: (LanguageConfig) -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissClick) {
        Surface(shape = RoundedCornerShape(24.dp)) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                TextMarquee(R.string.choose_language1)
                TextMarquee(R.string.choose_language2)
                TextMarquee(R.string.choose_language3)
                
                Spacer(modifier = Modifier.height(40.dp))
                
                languageList.forEach { item ->
                    LanguageRadioButton(
                        item = item,
                        onClick = { onLanguageClick(item.languageConfig) }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TextMarquee(@StringRes textId: Int) {
    Text(
        text = stringResource(textId),
        modifier = Modifier
            .fillMaxWidth()
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                animationMode = MarqueeAnimationMode.Immediately,
                delayMillis = 500,
                spacing = MarqueeSpacing.fractionOfContainer(0f)
            ),
        style = MaterialTheme.typography.displayLarge,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .2f)
    )
}

@Composable
private fun LanguageRadioButton(
    item: Language,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(34.dp))
            .background(color = Color.LightGray.copy(alpha = .5f))
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = .2f),
                shape = RoundedCornerShape(34.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.iconId),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
        Text(
            text = stringResource(id = item.nameId),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class Language(
    @DrawableRes val iconId: Int,
    @StringRes val nameId: Int,
    val languageConfig: LanguageConfig,
)

private val languageList = listOf(
    Language(
        iconId = AppIcons.DzFlag,
        nameId = R.string.arabic,
        languageConfig = LanguageConfig.ARABIC,
    ),
    Language(
        iconId = AppIcons.FrFlag,
        nameId = R.string.francais,
        languageConfig = LanguageConfig.FRENCH,
    ),
    Language(
        iconId = AppIcons.EnFlag,
        nameId = R.string.english,
        languageConfig = LanguageConfig.ENGLISH,
    ),
)
