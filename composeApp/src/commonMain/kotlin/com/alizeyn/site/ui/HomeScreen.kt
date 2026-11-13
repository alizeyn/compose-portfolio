package com.alizeyn.site.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.graphicsLayer
import com.alizeyn.site.data.SkillGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Profile
import com.alizeyn.site.data.about
import com.alizeyn.site.data.toolkit
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.hero_portrait
import com.alizeyn.site.resources.ic_camera
import com.alizeyn.site.resources.ic_language
import com.alizeyn.site.resources.ic_linkedin
import com.alizeyn.site.resources.ic_palette
import com.alizeyn.site.resources.ic_school
import com.alizeyn.site.resources.ic_work
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private data class Fact(val icon: DrawableResource, val title: String, val body: String)

private val facts = listOf(
    Fact(Res.drawable.ic_work, "Almost ten years · Four teams", "Shipping Android since 2017, from Java and XML to Kotlin and Compose, for apps used by millions."),
    Fact(Res.drawable.ic_school, "Studied", "B.Sc. Software Engineering, Ferdowsi University of Mashhad, 2015–2019."),
    Fact(Res.drawable.ic_language, "Languages", "Persian natively, English fluently, German at B1."),
    Fact(Res.drawable.ic_camera, "Off hours", "Generative art, producing music, photography, hiking, and a lot of tea."),
)

/** Home and About in one page: hero, pitch, story and facts, then the toolkit. */
@Composable
fun HomeScreen(widthClass: WidthClass, onEmail: () -> Unit, onWork: () -> Unit) {
    Page(widthClass) {
        Hero(widthClass, onEmail, onWork)
        About(widthClass)
        Reveal { DesignSystemCard(widthClass) }
        Toolkit(widthClass)
    }
}

@Composable
private fun Hero(widthClass: WidthClass, onEmail: () -> Unit, onWork: () -> Unit) {
    when (widthClass) {
        // Phones: portrait and the three chips share one row, then the copy below.
        WidthClass.Compact -> Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Reveal(pop = true) { Portrait(size = 128.dp) }
                // Chips hug the trailing edge, right-aligned.
                Reveal(delayMillis = 60) { HeroChips(onWork, stacked = true) }
            }
            HeroCopy(Modifier.fillMaxWidth(), onEmail, onWork, withChips = false)
        }
        else -> Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
            HeroCopy(Modifier.weight(1f), onEmail, onWork, withChips = true)
            Reveal(pop = true, delayMillis = 150) { Portrait(size = if (widthClass == WidthClass.Expanded) 260.dp else 200.dp) }
        }
    }
}

/** Role, focus and location chips, wrapping in a row or stacked in a column. */
@Composable
private fun HeroChips(onWork: () -> Unit, stacked: Boolean) {
    val uriHandler = LocalUriHandler.current
    val chips: @Composable () -> Unit = {
        AssistChip(
            onClick = onWork,
            label = { Text(Profile.role) },
            leadingIcon = {
                Icon(painterResource(Res.drawable.ic_work), contentDescription = null, modifier = Modifier.size(AssistChipDefaults.IconSize))
            },
        )
        AssistChip(
            onClick = onWork,
            label = { Text(Profile.focus) },
            leadingIcon = {
                Icon(painterResource(Res.drawable.ic_palette), contentDescription = null, modifier = Modifier.size(AssistChipDefaults.IconSize))
            },
        )
        AssistChip(
            onClick = { uriHandler.openUri("https://maps.google.com/?q=Vienna,+Austria") },
            label = { Text(Profile.location) },
            leadingIcon = {
                Icon(Icons.Filled.Place, contentDescription = null, modifier = Modifier.size(AssistChipDefaults.IconSize))
            },
        )
    }
    if (stacked) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.End) { chips() }
    } else {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { chips() }
    }
}

/**
 * Head-and-shoulders crop in the extra-large rounded rect, with a drifting gradient stroke. The
 * black-and-white photo is duotoned to the theme: deep green shadows on the paper tone in light,
 * near-black shadows with mint highlights in dark. The two ends animate with the theme toggle.
 */
@Composable
fun Portrait(size: Dp, shape: Shape = RoundedCornerShape(28.dp)) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val shadow by animateColorAsState(if (dark) Color(0xFF071009) else Color(0xFF0B3D27), tween(450), label = "shadow")
    val highlight by animateColorAsState(if (dark) Color(0xFFBFECD8) else Color(0xFFF1FDF0), tween(450), label = "highlight")
    Image(
        painter = painterResource(Res.drawable.hero_portrait),
        contentDescription = "Portrait of Ali, smiling, in front of a fern print",
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.colorMatrix(duotone(shadow, highlight)),
        modifier = Modifier
            .size(size)
            .border(3.dp, shimmerBrush(), shape)
            .padding(3.dp)
            .clip(shape),
    )
}

/** Maps luminance onto the ramp from [shadow] to [highlight]. */
private fun duotone(shadow: Color, highlight: Color): ColorMatrix {
    fun row(d: Float, l: Float) = floatArrayOf((l - d) * 0.299f, (l - d) * 0.587f, (l - d) * 0.114f, 0f, d * 255f)
    return ColorMatrix(
        row(shadow.red, highlight.red) + row(shadow.green, highlight.green) + row(shadow.blue, highlight.blue) +
            floatArrayOf(0f, 0f, 0f, 1f, 0f),
    )
}

@Composable
private fun HeroCopy(modifier: Modifier, onEmail: () -> Unit, onWork: () -> Unit, withChips: Boolean) {
    val uriHandler = LocalUriHandler.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (withChips) Reveal { HeroChips(onWork, stacked = false) }
        Reveal(delayMillis = 90) {
            Column {
                Text(Profile.firstName, style = MaterialTheme.typography.displayLargeEmphasized, color = MaterialTheme.colorScheme.onBackground)
                Text(Profile.lastName, style = MaterialTheme.typography.displayLargeEmphasized.copy(brush = shimmerBrush()))
            }
        }
        Reveal(delayMillis = 180) {
            Text(
                Profile.tagline,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(4.dp))
        Reveal(delayMillis = 270) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Medium expressive buttons: taller container, morphing shape on press.
            val height = ButtonDefaults.MediumContainerHeight
            Button(
                onClick = onEmail,
                shapes = ButtonDefaults.shapes(),
                modifier = Modifier.heightIn(height),
                contentPadding = ButtonDefaults.contentPaddingFor(height),
            ) {
                Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(ButtonDefaults.iconSizeFor(height)))
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(height)))
                Text("Email me", style = ButtonDefaults.textStyleFor(height))
            }
            FilledTonalButton(
                onClick = { uriHandler.openUri(Profile.linkedin) },
                shapes = ButtonDefaults.shapes(),
                modifier = Modifier.heightIn(height),
                contentPadding = ButtonDefaults.contentPaddingFor(height),
            ) {
                Icon(painterResource(Res.drawable.ic_linkedin), contentDescription = null, modifier = Modifier.size(ButtonDefaults.iconSizeFor(height)))
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(height)))
                Text("LinkedIn", style = ButtonDefaults.textStyleFor(height))
            }
            OutlinedButton(
                onClick = onWork,
                shapes = ButtonDefaults.shapes(),
                modifier = Modifier.heightIn(height),
                contentPadding = ButtonDefaults.contentPaddingFor(height),
            ) { Text("See my work", style = ButtonDefaults.textStyleFor(height)) }
        }
        }
    }
}

@Composable
private fun About(widthClass: WidthClass) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Reveal { SectionHeader("About · Development and product design", "Engineer & (Product) Designer") }
        if (widthClass == WidthClass.Expanded) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column(Modifier.weight(3f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    about.forEachIndexed { i, p -> Reveal(delayMillis = i * 80) { AccentParagraph(p) } }
                }
                Column(Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    facts.forEachIndexed { i, f -> Reveal(delayMillis = 120 + i * 80) { FactCard(f) } }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                about.forEachIndexed { i, p -> Reveal(delayMillis = i * 60) { AccentParagraph(p) } }
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                facts.forEachIndexed { i, f -> Reveal(delayMillis = i * 60) { FactCard(f) } }
            }
        }
    }
}

@Composable
private fun FactCard(fact: Fact) {
    OutlinedCard(Modifier.fillMaxWidth().hoverLift()) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(painterResource(fact.icon), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(fact.title, style = MaterialTheme.typography.titleMedium)
                Text(fact.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * The toolkit as a bento of four tonal cards, one per skill group. Each card wears a different
 * container colour and a slowly turning Material shape in its corner; the first three skills of
 * a group are set as solid pills, the rest as quiet ones, so the headline tools read first.
 */
@Composable
private fun Toolkit(widthClass: WidthClass) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Reveal { SectionHeader("Toolkit", "What I build with") }
        if (widthClass == WidthClass.Compact) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                toolkit.forEachIndexed { i, group -> Reveal(delayMillis = i * 60) { ToolkitCard(group, i) } }
            }
        } else {
            toolkit.chunked(2).forEachIndexed { rowIndex, pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                    pair.forEachIndexed { i, group ->
                        Reveal(Modifier.weight(1f), delayMillis = (rowIndex * 2 + i) * 60) {
                            ToolkitCard(group, rowIndex * 2 + i)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolkitCard(group: SkillGroup, index: Int, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val (container, ink) = listOf(
        scheme.primaryContainer to scheme.onPrimaryContainer,
        scheme.tertiaryContainer to scheme.onTertiaryContainer,
        scheme.secondaryContainer to scheme.onSecondaryContainer,
        scheme.surfaceContainerHighest to scheme.onSurface,
    )[index % 4]
    val decor = listOf(MaterialShapes.Sunny, MaterialShapes.Clover8Leaf, MaterialShapes.Cookie9Sided, MaterialShapes.Burst)[index % 4].toShape()
    val spin = rememberInfiniteTransition(label = "toolkitSpin")
    val angle by spin.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 36000 + index * 6000, easing = LinearEasing)),
        label = "toolkitAngle",
    )
    Box(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(container)
            .hoverLift(),
    ) {
        // Slowly turning shape, cut off by the card's corner.
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 28.dp, y = (-28).dp)
                .size(120.dp)
                .graphicsLayer { rotationZ = angle }
                .clip(decor)
                .background(ink.copy(alpha = 0.10f)),
        )
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(group.title, style = MaterialTheme.typography.headlineSmallEmphasized, color = ink)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                group.skills.forEachIndexed { i, skill ->
                    val solid = i < 3
                    Text(
                        skill,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (solid) container else ink,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (solid) ink else ink.copy(alpha = 0.10f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            }
        }
    }
}
