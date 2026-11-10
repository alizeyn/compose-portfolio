package com.alizeyn.site.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.alizeyn.site.resources.about_sky
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
    Fact(Res.drawable.ic_palette, "Leaning into", "Component libraries, Material 3, and the handshake between design and engineering."),
    Fact(Res.drawable.ic_work, "Mentoring", "Taught Android at Ferdowsi University; onboarded new developers at Neshan."),
    Fact(Res.drawable.ic_language, "Languages", "Persian natively, English fluently, German at B1."),
    Fact(Res.drawable.ic_camera, "Off hours", "Generative art, producing music, photography, hiking, and a lot of tea."),
)

/** Home and About in one page: hero, pitch, story and facts, then the toolkit. */
@Composable
fun HomeScreen(widthClass: WidthClass, onEmail: () -> Unit, onWork: () -> Unit) {
    Page(widthClass) {
        Hero(widthClass, onEmail, onWork)
        ShoutCard()
        About(widthClass)
        Toolkit()
    }
}

@Composable
private fun Hero(widthClass: WidthClass, onEmail: () -> Unit, onWork: () -> Unit) {
    when (widthClass) {
        WidthClass.Compact -> Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Portrait(size = 160.dp)
            HeroCopy(Modifier.fillMaxWidth(), onEmail, onWork)
        }
        else -> Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
            HeroCopy(Modifier.weight(1f), onEmail, onWork)
            Portrait(size = if (widthClass == WidthClass.Expanded) 260.dp else 200.dp)
        }
    }
}

/** Head-and-shoulders crop of the film-shop photo, in the extra-large rounded rect. */
@Composable
private fun Portrait(size: Dp) {
    Image(
        painter = painterResource(Res.drawable.hero_portrait),
        contentDescription = "Portrait of Ali in a film shop",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(size).clip(MaterialTheme.shapes.extraLarge),
    )
}

@Composable
private fun HeroCopy(modifier: Modifier, onEmail: () -> Unit, onWork: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
        Column {
            Text(Profile.firstName, style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onBackground)
            Text(Profile.lastName, style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.primary)
        }
        Text(
            Profile.tagline,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
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

@Composable
private fun ShoutCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(28.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(Profile.shoutTitle, style = MaterialTheme.typography.headlineMedium)
            Text(Profile.shoutBody, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun About(widthClass: WidthClass) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Banner(
            Res.drawable.about_sky,
            "A silhouette against the sky",
            height = if (widthClass == WidthClass.Compact) 180.dp else 240.dp,
            alignment = Alignment.BottomCenter,
        )
        SectionHeader("About", "Engineer with a designer's eye")
        if (widthClass == WidthClass.Expanded) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column(Modifier.weight(3f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    about.forEach { AccentParagraph(it) }
                }
                Column(Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    facts.forEach { FactCard(it) }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                about.forEach { AccentParagraph(it) }
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                facts.forEach { FactCard(it) }
            }
        }
    }
}

@Composable
private fun FactCard(fact: Fact) {
    OutlinedCard(Modifier.fillMaxWidth()) {
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
private fun Toolkit() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader("Toolkit", "What I build with")
        toolkit.forEach { group ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(group.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.skills.forEach { SuggestionChip(onClick = {}, label = { Text(it) }) }
                }
            }
        }
    }
}
