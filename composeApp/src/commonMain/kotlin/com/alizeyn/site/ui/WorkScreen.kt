package com.alizeyn.site.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.layout.ContentScale
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
                                RoleCard(role, rowIndex * 2 + i, Modifier.fillMaxHeight())
                            }
                        }
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                roles.forEachIndexed { i, role -> Reveal(delayMillis = i * 70) { RoleCard(role, i) } }
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

/** The apps as store icons; each opens its Play Store listing. */
@Composable
private fun AppShelf(widthClass: WidthClass) {
    val uriHandler = LocalUriHandler.current
    if (widthClass == WidthClass.Compact) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            apps.forEachIndexed { i, app -> Reveal(delayMillis = i * 60) { AppRow(app, onOpen = { uriHandler.openUri(app.playUrl) }) } }
        }
    } else {
        // Cards share each row evenly: all five across on desktop, three per row on tablets.
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = if (widthClass == WidthClass.Expanded) 5 else 3,
        ) {
            apps.forEachIndexed { i, app ->
                Reveal(Modifier.weight(1f), delayMillis = i * 60) { AppCard(app, onOpen = { uriHandler.openUri(app.playUrl) }) }
            }
        }
    }
}

@Composable
private fun AppIcon(app: App, size: Dp) {
    Image(
        painter = painterResource(app.icon),
        contentDescription = "${app.name} icon",
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(size).clip(RoundedCornerShape(size * 0.22f)),
    )
}

@Composable
private fun AppCard(app: App, onOpen: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().hoverLift().clickable(onClick = onOpen)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppIcon(app, 64.dp)
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(app.name, style = MaterialTheme.typography.titleMedium)
                Text(app.company, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
            Text(app.blurb, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            FilledTonalButton(onClick = onOpen, shapes = ButtonDefaults.shapes(), contentPadding = ButtonDefaults.ButtonWithIconContentPadding) {
                Text("Play Store")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(painterResource(Res.drawable.ic_open_in_new), contentDescription = "opens in a new tab", modifier = Modifier.size(ButtonDefaults.IconSize))
            }
        }
    }
}

@Composable
private fun AppRow(app: App, onOpen: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().hoverLift().clickable(onClick = onOpen)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AppIcon(app, 56.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(app.name, style = MaterialTheme.typography.titleMedium)
                Text(app.company, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(app.blurb, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(painterResource(Res.drawable.ic_open_in_new), contentDescription = "Open on the Play Store", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        }
    }
}
/**
 * In the style of the cards on m3.material.io: a coloured header band carrying the logo, company
 * and title, with an expressive shape peeking in from the edge, and the body text below.
 * The header opens the company's LinkedIn page.
 */
@Composable
private fun RoleCard(role: Role, index: Int, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val scheme = MaterialTheme.colorScheme
    val (header, onHeader) = listOf(
        scheme.primaryContainer to scheme.onPrimaryContainer,
        scheme.tertiaryContainer to scheme.onTertiaryContainer,
        scheme.secondaryContainer to scheme.onSecondaryContainer,
        scheme.surfaceContainerHighest to scheme.onSurface,
    )[index % 4]
    val decoration = listOf(MaterialShapes.Cookie9Sided, MaterialShapes.Sunny, MaterialShapes.Clover8Leaf, MaterialShapes.Pill)[index % 4].toShape()

    ElevatedCard(modifier.fillMaxWidth().hoverLift(), shape = MaterialTheme.shapes.extraLarge) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(116.dp)
                    .background(header)
                    .clipToBounds()
                    .clickable { uriHandler.openUri(role.linkedin) },
                contentAlignment = Alignment.CenterStart,
            ) {
                Box(
                    Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 40.dp, y = 30.dp)
                        .size(150.dp)
                        .clip(decoration)
                        .background(onHeader.copy(alpha = 0.10f)),
                )
                Row(
                    Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(scheme.surfaceContainerLowest),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(role.logo),
                            contentDescription = "${role.company} logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.small),
                        )
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(role.company, style = MaterialTheme.typography.titleLargeEmphasized, color = onHeader)
                        Text(role.title, style = MaterialTheme.typography.bodyMedium, color = onHeader.copy(alpha = 0.85f))
                        Text(role.period, style = MaterialTheme.typography.labelMedium, color = onHeader.copy(alpha = 0.7f))
                    }
                    Icon(
                        painterResource(Res.drawable.ic_open_in_new),
                        contentDescription = "${role.company} on LinkedIn",
                        tint = onHeader.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
}
