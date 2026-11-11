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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
 * Role card after the m3.material.io news card: a flat surface-container-low card with 24dp
 * corners, a solid header in a deep shade of the company logo carrying the company, role title and date line, then a
 * 24dp-padded body of description, highlights and stack. The header opens the company's
 * LinkedIn page.
 */
@Composable
private fun RoleCard(role: Role, index: Int, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val corner = RoundedCornerShape(24.dp)
    val field = role.brand
    val ink = role.brandInk
    val scheme = MaterialTheme.colorScheme
    val darkTheme = scheme.surface.luminance() < 0.5f
    // The body takes a faded version of the header: a wash of the light ink on light surfaces,
    // a deeper wash of the brand colour itself on dark ones.
    val body = if (darkTheme) lerp(scheme.surfaceContainerLow, field, 0.45f) else lerp(scheme.surfaceContainerLow, ink, 0.16f)

    Card(
        modifier = modifier.fillMaxWidth().hoverLift(),
        shape = corner,
        colors = CardDefaults.cardColors(containerColor = body),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(corner)
                    .background(field)
                    .dotPattern(ink)
                    .clickable { uriHandler.openUri(role.linkedin) }
                    .padding(horizontal = 24.dp, vertical = 20.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.size(36.dp).clip(MaterialTheme.shapes.small).background(Color.White), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(role.logo),
                            contentDescription = "${role.company} logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(26.dp).clip(MaterialTheme.shapes.extraSmall),
                        )
                    }
                    Text(
                        role.company,
                        fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold,
                        color = ink,
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    role.title,
                    fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium,
                    color = ink,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${role.period} · ${role.arrangement} · ${role.place}",
                    fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal,
                    color = ink.copy(alpha = 0.8f),
                )
            }
            Column(Modifier.padding(24.dp)) {
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

/** A quiet dot grid in the ink colour, 12dp apart, that gives the header some texture. */
private fun Modifier.dotPattern(ink: Color): Modifier = drawBehind {
    val step = 12.dp.toPx()
    val r = 1.dp.toPx()
    val color = ink.copy(alpha = 0.22f)
    var y = step / 2f
    while (y < size.height) {
        var x = step / 2f
        while (x < size.width) { drawCircle(color, r, Offset(x, y)); x += step }
        y += step
    }
}
