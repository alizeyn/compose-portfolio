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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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
 * A faithful copy of the news cards on m3.material.io (measured from the live page): a flat
 * surface-container-low card with 24dp corners, a 2:1 banner framed by a 14 × 7 grid whose middle
 * window is left clear and carries the company set large, then a 24dp-padded stack of
 * date (16/24), title (24/32 medium) and description (16/24, on-surface-variant) with 8dp gaps.
 * The banner opens the company's LinkedIn page.
 */
@Composable
private fun RoleCard(role: Role, index: Int, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    // Field, grid line, title. The third set is the reference card's own purple.
    val (field, line, ink) = listOf(
        Triple(Color(0xFF0D2B1C), Color(0xFF3FBF7F), Color(0xFF5CFF9F)),
        Triple(Color(0xFF0E2A2A), Color(0xFF4FA8A0), Color(0xFF8DD3C8)),
        Triple(Color(0xFF2A0C35), Color(0xFFA46BD6), Color(0xFFED9EFF)),
        Triple(Color(0xFF2A1B10), Color(0xFFC47A3C), Color(0xFFFFB874)),
    )[index % 4]
    val corner = RoundedCornerShape(24.dp)

    Card(
        modifier = modifier.fillMaxWidth().hoverLift(),
        shape = corner,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column {
            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(584f / 298f)
                    .clip(corner)
                    .background(field)
                    .gridFrame(field, line)
                    .clickable { uriHandler.openUri(role.linkedin) },
                contentAlignment = Alignment.Center,
            ) {
                val bannerWidth = maxWidth
                val titleSize = (bannerWidth.value * 0.1f).sp
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(Modifier.size(bannerWidth * 0.075f).clip(MaterialTheme.shapes.small).background(Color.White), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(role.logo),
                            contentDescription = "${role.company} logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(bannerWidth * 0.056f).clip(MaterialTheme.shapes.extraSmall),
                        )
                    }
                    Text(
                        role.company,
                        fontSize = titleSize,
                        lineHeight = titleSize,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = (-0.5).sp,
                        color = ink,
                        softWrap = false,
                    )
                }
            }
            Column(Modifier.padding(24.dp)) {
                Text(
                    "${role.period} · ${role.arrangement} · ${role.place}",
                    fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    role.title,
                    fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    role.summary,
                    fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    role.highlights.forEach { line ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("•", fontSize = 16.sp, lineHeight = 24.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(10.dp))
                            Text(line, fontSize = 16.sp, lineHeight = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    role.stack.forEach { SuggestionChip(onClick = {}, label = { Text(it) }, modifier = Modifier.hoverLift(lift = 1.dp)) }
                }
            }
        }
    }
}

/**
 * The reference banner's grid: 14 columns by 7 rows of square cells, vertical lines on the
 * half-cell, horizontal lines on the cell, and a clear window inset 1.5 cells horizontally and
 * one cell vertically. Measured from the "Compose First" illustration.
 */
private fun Modifier.gridFrame(field: Color, line: Color): Modifier = drawBehind {
    val cell = size.width / 14f
    val stroke = 1.dp.toPx()
    var x = cell / 2f
    while (x < size.width) { drawLine(line, Offset(x, 0f), Offset(x, size.height), stroke); x += cell }
    var y = cell
    while (y < size.height) { drawLine(line, Offset(0f, y), Offset(size.width, y), stroke); y += cell }
    drawRect(field, topLeft = Offset(cell * 1.5f, cell), size = Size(size.width - cell * 3f, size.height - cell * 2f))
}
