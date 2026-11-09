package com.alizeyn.site.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Role
import com.alizeyn.site.data.Tone
import com.alizeyn.site.data.roles
import com.alizeyn.site.data.rolesFootnote
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.experience_facade
import com.alizeyn.site.theme.ToneColors
import com.alizeyn.site.theme.toneBlue
import com.alizeyn.site.theme.toneMaroon
import com.alizeyn.site.theme.toneOrange

@Composable
private fun Tone.colors(): ToneColors {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    return when (this) {
        Tone.Orange -> toneOrange(dark)
        Tone.Blue -> toneBlue(dark)
        Tone.Maroon -> toneMaroon(dark)
    }
}

@Composable
fun WorkScreen(widthClass: WidthClass) {
    Page(widthClass) {
        Banner(Res.drawable.experience_facade, "A facade in Graz", height = if (widthClass == WidthClass.Compact) 180.dp else 260.dp)
        SectionHeader("Work", "Four teams, one direction")
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            roles.forEach { RoleCard(it) }
        }
        Text(
            "* $rolesFootnote",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun RoleCard(role: Role) {
    val tone = role.tone.colors()
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier.size(44.dp).background(tone.container, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        role.company.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = tone.onContainer,
                    )
                }
                Column {
                    Text(role.company, style = MaterialTheme.typography.titleLarge)
                    Text(role.period, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            role.body.forEach { paragraph ->
                Text(paragraph, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
