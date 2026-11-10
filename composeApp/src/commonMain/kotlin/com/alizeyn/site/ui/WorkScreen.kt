package com.alizeyn.site.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Role
import com.alizeyn.site.data.roles
import com.alizeyn.site.data.rolesFootnote
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkScreen(widthClass: WidthClass) {
    Page(widthClass) {
        Reveal { SectionHeader("Work", "Ten years, four teams") }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            roles.forEachIndexed { i, role -> Reveal(delayMillis = i * 70) { RoleCard(role) } }
        }
        Text(
            rolesFootnote,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun RoleCard(role: Role) {
    ElevatedCard(Modifier.fillMaxWidth().hoverLift()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    Modifier
                        .size(52.dp)
                        .hoverLift(scale = 1.1f, lift = 0.dp)
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
                Column {
                    Text(role.company, style = MaterialTheme.typography.titleLarge)
                    Text(
                        "${role.title} · ${role.period}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
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
                role.stack.forEach { SuggestionChip(onClick = {}, label = { Text(it) }, modifier = Modifier.hoverLift(scale = 1.06f, lift = 1.dp)) }
            }
        }
    }
}
