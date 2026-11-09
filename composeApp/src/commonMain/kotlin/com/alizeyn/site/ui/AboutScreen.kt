package com.alizeyn.site.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.about
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.about_sky
import com.alizeyn.site.resources.ic_camera
import com.alizeyn.site.resources.ic_language
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

@Composable
fun AboutScreen(widthClass: WidthClass) {
    Page(widthClass) {
        Banner(
            Res.drawable.about_sky,
            "A silhouette against the sky",
            height = if (widthClass == WidthClass.Compact) 200.dp else 280.dp,
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
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                facts.forEach { fact ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(fact.title) },
                        icon = {
                            Icon(painterResource(fact.icon), contentDescription = null, modifier = Modifier.size(SuggestionChipDefaults.IconSize))
                        },
                    )
                }
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
