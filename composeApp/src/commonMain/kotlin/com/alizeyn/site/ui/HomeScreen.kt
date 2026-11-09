package com.alizeyn.site.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Profile
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.hero_filmshop
import com.alizeyn.site.resources.ic_open_in_new
import com.alizeyn.site.resources.ic_palette
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(widthClass: WidthClass, onEmail: () -> Unit, onAbout: () -> Unit) {
    Page(widthClass) {
        if (widthClass == WidthClass.Expanded) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
                HeroCopy(Modifier.weight(3f), onEmail, onAbout)
                HeroPhoto(Modifier.weight(2f))
            }
        } else {
            HeroPhoto(Modifier.fillMaxWidth(), landscape = widthClass == WidthClass.Medium)
            HeroCopy(Modifier.fillMaxWidth(), onEmail, onAbout)
        }
        ShoutCard()
    }
}

@Composable
private fun HeroPhoto(modifier: Modifier, landscape: Boolean = false) {
    Image(
        painter = painterResource(Res.drawable.hero_filmshop),
        contentDescription = "Ali in a film shop",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .then(if (landscape) Modifier.height(360.dp) else Modifier.aspectRatio(4f / 5f))
            .clip(MaterialTheme.shapes.extraLarge),
    )
}

@Composable
private fun HeroCopy(modifier: Modifier, onEmail: () -> Unit, onAbout: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = onAbout,
                label = { Text(Profile.role) },
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
            Profile.tagline.joinToString(" "),
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
            OutlinedButton(onClick = onAbout) { Text("About me") }
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
            Text(Profile.shout.joinToString(" "), style = MaterialTheme.typography.headlineMedium)
            Text(
                "Eight-odd years of shipping mobile apps, now pointed at product design. The portfolio is the poster set; this is the same story told in Material.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
