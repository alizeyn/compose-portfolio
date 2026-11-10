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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
 * In the style of the cards on m3.material.io: a tall dark banner framed by a grid pattern with
 * the company set large in the middle, then a plain text body: dates, title, summary and the
 * details. The banner opens the company's LinkedIn page.
 */
@Composable
private fun RoleCard(role: Role, index: Int, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val (field, ink) = listOf(
        Color(0xFF0D2B1C) to Color(0xFF5CFF9F),
        Color(0xFF0E2A2A) to Color(0xFF8DD3C8),
        Color(0xFF1B1540) to Color(0xFFCDA4FF),
        Color(0xFF2A1B10) to Color(0xFFFFB874),
    )[index % 4]

    ElevatedCard(modifier.fillMaxWidth().hoverLift(), shape = MaterialTheme.shapes.extraLarge) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .background(field)
                    .gridFrame(field, ink.copy(alpha = 0.55f))
                    .clickable { uriHandler.openUri(role.linkedin) },
                contentAlignment = Alignment.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(Modifier.size(48.dp).clip(MaterialTheme.shapes.medium).background(Color.White), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(role.logo),
                            contentDescription = "${role.company} logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(36.dp).clip(MaterialTheme.shapes.extraSmall),
                        )
                    }
                    Text(role.company, style = MaterialTheme.typography.displaySmallEmphasized, color = ink)
                }
                Icon(
                    painterResource(Res.drawable.ic_open_in_new),
                    contentDescription = "${role.company} on LinkedIn",
                    tint = ink.copy(alpha = 0.8f),
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(18.dp),
                )
            }
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(role.period, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(role.title, style = MaterialTheme.typography.headlineSmallEmphasized, color = MaterialTheme.colorScheme.onSurface)
                Text(role.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
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

/** The m3.material.io banner grid: thin cells around the edge, the middle left as a clear window. */
private fun Modifier.gridFrame(field: Color, line: Color): Modifier = drawBehind {
    val cell = 44.dp.toPx()
    val stroke = 1.dp.toPx()
    var x = 0f
    while (x <= size.width + 1f) { drawLine(line, Offset(x, 0f), Offset(x, size.height), stroke); x += cell }
    var y = 0f
    while (y <= size.height + 1f) { drawLine(line, Offset(0f, y), Offset(size.width, y), stroke); y += cell }
    val insetX = cell * 1.5f
    val insetY = cell * 1f
    drawRect(field, topLeft = Offset(insetX, insetY), size = Size(size.width - 2 * insetX, size.height - 2 * insetY))
}
