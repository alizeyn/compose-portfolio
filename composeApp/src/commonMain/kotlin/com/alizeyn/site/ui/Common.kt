package com.alizeyn.site.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Paragraph
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/** Material 3 layout margins: 16dp compact, 24dp medium and expanded. */
fun WidthClass.margin(): Dp = if (this == WidthClass.Compact) 16.dp else 24.dp

/** A scrolling page column capped at a readable measure and centred. */
@Composable
fun Page(widthClass: WidthClass, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(PaddingValues(horizontal = widthClass.margin()))
            .widthIn(max = 1040.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Spacer(Modifier.height(if (widthClass == WidthClass.Compact) 0.dp else 24.dp))
        content()
        Footer()
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun SectionHeader(eyebrow: String, title: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(eyebrow, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun Banner(
    image: DrawableResource,
    contentDescription: String,
    height: Dp,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
) {
    Image(
        painter = painterResource(image),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        alignment = alignment,
        modifier = modifier.fillMaxWidth().height(height).clip(MaterialTheme.shapes.extraLarge),
    )
}

@Composable
fun AccentParagraph(paragraph: Paragraph, modifier: Modifier = Modifier) {
    val accent = MaterialTheme.colorScheme.primary
    val text = buildAnnotatedString {
        paragraph.forEach { segment ->
            if (segment.accent) {
                withStyle(SpanStyle(color = accent, fontWeight = FontWeight.SemiBold)) { append(segment.text) }
            } else {
                append(segment.text)
            }
        }
    }
    Text(text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, modifier = modifier)
}

@Composable
private fun Footer() {
    Text(
        "Drawn with Compose Multiplatform for Web and Material 3. © Ali Zeynali",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 24.dp),
    )
}
