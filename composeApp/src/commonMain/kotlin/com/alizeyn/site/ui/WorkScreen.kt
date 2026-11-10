package com.alizeyn.site.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.App
import com.alizeyn.site.data.Role
import com.alizeyn.site.data.apps
import com.alizeyn.site.data.roles
import com.alizeyn.site.data.rolesFootnote
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.ic_open_in_new
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkScreen(widthClass: WidthClass) {
    Page(widthClass) {
        Reveal { SectionHeader("Work", "Ten years, four teams") }
        if (widthClass == WidthClass.Expanded) {
            // Two cards per row on desktop; rows stretch so both cards share a height.
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                roles.chunked(2).forEachIndexed { rowIndex, pair ->
                    Row(Modifier.height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        pair.forEachIndexed { i, role ->
                            Reveal(Modifier.weight(1f).fillMaxHeight(), delayMillis = (rowIndex * 2 + i) * 70) {
                                RoleCard(role, Modifier.fillMaxHeight())
                            }
                        }
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                roles.forEachIndexed { i, role -> Reveal(delayMillis = i * 70) { RoleCard(role) } }
            }
        }
        Text(
            rolesFootnote,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Reveal { SectionHeader("Apps", "On the Play Store") }
        AppShelf(widthClass)
    }
}

/** The apps in Pixel frames, side by side; each opens its Play Store listing. */
@Composable
private fun AppShelf(widthClass: WidthClass) {
    val uriHandler = LocalUriHandler.current
    val frameWidth = if (widthClass == WidthClass.Compact) 150.dp else 170.dp
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(apps) { app -> AppCard(app, frameWidth, onOpen = { uriHandler.openUri(app.playUrl) }) }
    }
}

@Composable
private fun AppCard(app: App, frameWidth: Dp, onOpen: () -> Unit) {
    Reveal(pop = true) {
        Column(Modifier.width(frameWidth + 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            PixelFrame(
                screenshot = app.screenshot,
                contentDescription = "${app.name} on a Pixel",
                width = frameWidth,
                modifier = Modifier.hoverLift(lift = 6.dp).clickable(onClick = onOpen),
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(app.name, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                Text(app.company, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(app.blurb, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            }
            FilledTonalButton(onClick = onOpen, contentPadding = ButtonDefaults.ButtonWithIconContentPadding) {
                Text("Play Store")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(painterResource(Res.drawable.ic_open_in_new), contentDescription = "opens in a new tab", modifier = Modifier.size(ButtonDefaults.IconSize))
            }
        }
    }
}

@Composable
private fun RoleCard(role: Role, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    ElevatedCard(modifier.fillMaxWidth().hoverLift()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Logo, company and title open the company's LinkedIn page.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { uriHandler.openUri(role.linkedin) }
                    .padding(4.dp),
            ) {
                Box(
                    Modifier
                        .size(52.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(role.logo),
                        contentDescription = "${role.company} logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.small),
                    )
                }
                Column(Modifier.weight(1f)) {
                    Text(role.company, style = MaterialTheme.typography.titleLarge)
                    Text(
                        "${role.title} · ${role.period}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    painterResource(Res.drawable.ic_open_in_new),
                    contentDescription = "${role.company} on LinkedIn",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text(role.arrangement) },
                    leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null, modifier = Modifier.size(AssistChipDefaults.IconSize)) },
                )
                AssistChip(
                    onClick = {},
                    label = { Text(role.place) },
                    leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null, modifier = Modifier.size(AssistChipDefaults.IconSize)) },
                )
            }
            Text(role.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                role.highlights.forEach { line ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("•", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(10.dp))
                        Text(line, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                role.stack.forEach { SuggestionChip(onClick = {}, label = { Text(it) }, modifier = Modifier.hoverLift(lift = 1.dp)) }
            }
        }
    }
}
