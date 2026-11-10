package com.alizeyn.site.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.alizeyn.site.resources.ic_open_in_new
import com.alizeyn.site.resources.ic_palette
import com.alizeyn.site.resources.ic_school
import com.alizeyn.site.resources.ic_work
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private data class Fact(val icon: DrawableResource, val title: String, val body: String)

private val facts = listOf(
    Fact(Res.drawable.ic_school, "Studied", "B.Sc. Software Engineering, Ferdowsi University of Mashhad, 2015–2019."),
    Fact(Res.drawable.ic_language, "Languages", "Persian natively, English fluently, German at B1."),
    Fact(Res.drawable.ic_camera, "Off hours", "Generative art, producing music, photography, hiking, and a lot of tea."),
)

/** Home and About in one page: hero, pitch, story and facts, then the toolkit. */
@Composable
fun HomeScreen(widthClass: WidthClass, onEmail: () -> Unit, onWork: () -> Unit) {
    Page(widthClass) {
        Hero(widthClass, onEmail, onWork)
        Reveal { DesignSystemCard(widthClass) }
        About(widthClass)
        Toolkit(widthClass)
    }
}

@Composable
private fun Hero(widthClass: WidthClass, onEmail: () -> Unit, onWork: () -> Unit) {
    when (widthClass) {
        WidthClass.Compact -> Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Reveal(pop = true) { Portrait(size = 160.dp) }
            HeroCopy(Modifier.fillMaxWidth(), onEmail, onWork)
        }
        else -> Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
            HeroCopy(Modifier.weight(1f), onEmail, onWork)
            Reveal(pop = true, delayMillis = 150) { Portrait(size = if (widthClass == WidthClass.Expanded) 260.dp else 200.dp) }
        }
    }
}

/** Head-and-shoulders crop in the extra-large rounded rect, with a drifting gradient stroke. */
@Composable
private fun Portrait(size: Dp) {
    val shape = MaterialTheme.shapes.extraLarge
    Image(
        painter = painterResource(Res.drawable.hero_portrait),
        contentDescription = "Portrait of Ali, smiling, in front of a fern print",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .border(3.dp, shimmerBrush(), shape)
            .padding(3.dp)
            .clip(shape),
    )
}

@Composable
private fun HeroCopy(modifier: Modifier, onEmail: () -> Unit, onWork: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Reveal {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = onWork,
                label = { Text(Profile.role) },
                leadingIcon = {
                    Icon(
                        painterResource(Res.drawable.ic_work),
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                    )
                },
            )
            AssistChip(
                onClick = onWork,
                label = { Text(Profile.focus) },
                leadingIcon = {
                    Icon(
                        painterResource(Res.drawable.ic_palette),
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                    )
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
        }
        Reveal(delayMillis = 90) {
            Column {
                Text(Profile.firstName, style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onBackground)
                Text(Profile.lastName, style = MaterialTheme.typography.displayLarge.copy(brush = shimmerBrush()))
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
            Button(onClick = onEmail, contentPadding = ButtonDefaults.ButtonWithIconContentPadding) {
                Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Email me")
            }
            FilledTonalButton(
                onClick = { uriHandler.openUri(Profile.linkedin) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Text("LinkedIn")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(painterResource(Res.drawable.ic_open_in_new), contentDescription = "opens in a new tab", modifier = Modifier.size(ButtonDefaults.IconSize))
            }
            OutlinedButton(onClick = onWork) { Text("See my work") }
        }
        }
    }
}

@Composable
private fun About(widthClass: WidthClass) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Reveal { SectionHeader("About", "Engineer with a designer's eye") }
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

@Composable
private fun Toolkit(widthClass: WidthClass) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Reveal { SectionHeader("Toolkit", "What I build with") }
        toolkit.forEachIndexed { groupIndex, group ->
            Reveal(delayMillis = groupIndex * 50) {
                val chips: @Composable () -> Unit = {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        group.skills.forEach { skill ->
                            SuggestionChip(onClick = {}, label = { Text(skill, style = MaterialTheme.typography.labelMedium) }, modifier = Modifier.hoverLift(lift = 1.dp))
                        }
                    }
                }
                if (widthClass == WidthClass.Compact) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(group.title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        chips()
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(group.title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(132.dp))
                        chips()
                    }
                }
            }
        }
    }
}
